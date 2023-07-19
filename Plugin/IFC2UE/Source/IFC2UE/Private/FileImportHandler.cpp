// Fill out your copyright notice in the Description page of Project Settings.


#include "FileImportHandler.h"
#include "ToolMenus.h"
#include "Developer/DesktopPlatform/Public/IDesktopPlatform.h"
#include "Developer/DesktopPlatform/Public/DesktopPlatformModule.h"
#include "Serialization/JsonReader.h"
#include "Serialization/JsonSerializer.h"
#include "Factories/FbxFactory.h"
#include <Runtime/Core/Public/Misc/FileHelper.h>
#include <fstream>
#include <Editor/UnrealEd/Public/ObjectTools.h>
#include "CoreMinimal.h"
#include "Factories/FbxImportUI.h"
#include "Factories/FbxStaticMeshImportData.h"
#include "UObject/NameTypes.h"
#include "UObject/ConstructorHelpers.h"
#include "AssetImportTask.h"
#include <Runtime/UMG/Public/Components/Viewport.h>
#include <AssetRegistryModule.h>
#include "AssetToolsModule.h"
#include "Building.h"
#include "BuildingAssetData.h"
#include "UObject/Object.h"
#include "GameplayTagContainer.h"
#include "IFC2UE.h"
#include "LightAndSwitch.h"
#include "UserInterface.h"
#include "AssetTools/Private/AssetTools.h"
#include "Framework/Notifications/NotificationManager.h"
#include "Materials/MaterialInstanceDynamic.h"
#include "Widgets/Notifications/SNotificationList.h"

UMaterial* GM_MyMaterial;
#define LOCTEXT_NAMESPACE "FIFC_2_UEModule"

UserInterface* UserInt = new UserInterface();

FileImportHandler::FileImportHandler()
{
	GM_MyMaterial = LoadObject<UMaterial>(
		nullptr, TEXT("Material'/Game/StarterContent/Materials/M_Ground_Grass.M_Ground_Grass'"));
}

FileImportHandler::~FileImportHandler()
{
}

void FileImportHandler::OpenFileDialog(TArray<FString>& OutFileNames, const void* ParentWindowHandle)
{
	FHttpModule* Http = &FHttpModule::Get();
	if (IDesktopPlatform* DesktopPlatform = FDesktopPlatformModule::Get())
	{
		//Opening the file picker!
		constexpr uint32 SelectionFlag = 0;
		//A value of 0 represents single file selection while a value of 1 represents multiple file selection
		DesktopPlatform->OpenFileDialog(ParentWindowHandle, "IFC File Selector", "", FString(""), "IFC Files|*.ifc",
		                                SelectionFlag, OutFileNames);
		if (OutFileNames.Num() != 0)
		{
			TArray<uint8> UpFileRawData;
			FString FileAsString;
			FFileHelper::LoadFileToArray(UpFileRawData, *OutFileNames[0]);
			FFileHelper::LoadFileToString(FileAsString, *OutFileNames[0]);
			// prepare json data
			FString JsonString;
			const TSharedRef<TJsonWriter<TCHAR>> JsonWriter = TJsonWriterFactory<TCHAR>::Create(&JsonString);

			JsonWriter->WriteObjectStart();
			JsonWriter->WriteValue("fileName", OutFileNames[0]);
			JsonWriter->WriteValue("fileContent", FileAsString);
			// JsonWriter->WriteValue("file", UpFileRawData);
			JsonWriter->WriteObjectEnd();
			JsonWriter->Close();

			// the json request
			const TSharedRef<IHttpRequest, ESPMode::ThreadSafe> SendJsonRequest = Http->CreateRequest();
			SendJsonRequest->OnProcessRequestComplete().BindRaw(this, &FileImportHandler::GetFileAndInformation);
			SendJsonRequest->SetURL("http://localhost:8080/convert/uploadIFC");
			SendJsonRequest->SetVerb("POST");
			SendJsonRequest->SetHeader("Content-Type", " application/json");
			SendJsonRequest->SetTimeout(6000);
			//SendJsonRequest->SetHeader("Content-Type", "multipart/form-data");
			SendJsonRequest->SetContentAsString(JsonString);
			//SendJsonRequest->SetContentAsStreamedFile(OutFileNames[0]);
			if (!SendJsonRequest->ProcessRequest())
			{
				FNotificationInfo Info(FText::FromString(TEXT("Could not send request!")));
				Info.ExpireDuration = 5.0f;
				FSlateNotificationManager::Get().AddNotification(Info);
			}
			else
			{
				LOCTEXT("UploadIFCFile", "Conversion from Server is starting...");
			}
		}
	}
}

void FileImportHandler::GetFileAndInformation(FHttpRequestPtr Request, FHttpResponsePtr Response,
                                              bool bConnectedSuccessfully) const
{
	if (Response->GetResponseCode() == 200)
	{
		TSharedPtr<FJsonObject> JsonObject;
		const FString ResponseBody = Response->GetContentAsString();
		if (TSharedRef<TJsonReader<>> Reader = TJsonReaderFactory<>::Create(ResponseBody); FJsonSerializer::Deserialize(Reader, JsonObject))
		{
			FString FileContent = JsonObject->GetStringField("fileContent");
			// TArray<TMap<FString, FString>> information = JsonObject->GetArrayField("information");
			TSharedPtr<FJsonObject> Information = JsonObject->GetObjectField("information");

			TMap<FString, TSharedPtr<FJsonValue>> LightObjects = Information->GetObjectField("lightObjects")->Values;
			const FString FileName = Information->GetStringField("buildingName");
			const TArray<TSharedPtr<FJsonValue>>& LightArray = Information->GetArrayField("lightObjects");
			TMap<FString, TSharedPtr<FJsonValue>> Materials = Information->GetObjectField("materials")->Values;

			TArray<FLightAndSwitch> Lights;
			for (const TSharedPtr<FJsonValue>& LightValue : LightArray)
			{
				// Get the JSON object for the current light
				const TSharedPtr<FJsonObject>& LightObject = LightValue->AsObject();

				// Extract the values for each of the properties
				FString LightName = LightObject->GetStringField("lightObject");
				FString LightSwitch = LightObject->GetStringField("lightSwitch");
				double Voltage = LightObject->GetNumberField("voltage");

				// Create a new light object and add it to the list
				Lights.Add(FLightAndSwitch(LightName, LightSwitch, Voltage));
			}

			FILE* FP = tmpfile();
			FString OutFile = FileName + ".fbx";
			const char* t = TCHAR_TO_ANSI(*OutFile);
			std::ofstream Outfile(t);
			Outfile << TCHAR_TO_UTF8(*FileContent) << std::endl;
			Outfile.close();

			FBXFileImport(FileName, Lights, Materials);
		}
	}
	else
	{
		FNotificationInfo Info(FText::FromString("Error: Connection failed"));
		//Set a default expire duration
		Info.ExpireDuration = 5.0f;
		FSlateNotificationManager::Get().AddNotification(Info);
	}
}


// Import FBX from specified path functions
void FileImportHandler::FBXFileImport(FString FileName, TArray<FLightAndSwitch> Lights, TMap<FString, TSharedPtr<FJsonValue>> Materials) const
{
	const FString FBXFilePath = FileName + ".fbx";
	FString PackageName = "/Game/IFC2UE/";
	FAssetToolsModule& AssetToolsModule = FModuleManager::Get().LoadModuleChecked<FAssetToolsModule>("AssetTools");
	AssetToolsModule.Get().CreateUniqueAssetName(TEXT("/Game/IFC2UE/" + FileName), TEXT(""), PackageName, FileName);

	bool bIsCancelled = false;
	constexpr bool bShowFBXOptionsDialog = false;
	FString FBXFileImportFolder = PackageName + "/";
	FString FBXFileImportDestFolder = FBXFileImportFolder + "Meshes";
	//UserInterface* ui = NewObject<UserInterface>();
	UserInt->UpdateUploadText(FText::FromString(TEXT("FBX File received. Starting uploading it to Unreal Engine...")));
	UFbxFactory* TmpFactory = NewObject<UFbxFactory>(UFbxFactory::StaticClass(), FName("Factory"));
	UAssetImportTask* TmpImportTask = NewObject<UAssetImportTask>(UAssetImportTask::StaticClass());

	TmpFactory->AddToRoot();
	TmpFactory->ImportUI->MeshTypeToImport = FBXIT_StaticMesh;
	TmpFactory->ImportUI->StaticMeshImportData->bCombineMeshes = false;
	TmpFactory->ImportUI->StaticMeshImportData->bImportAsScene = true;
	TmpFactory->ImportUI->StaticMeshImportData->bConvertScene = true;
	TmpFactory->ImportUI->bImportMaterials = true;
	TmpFactory->ImportUI->StaticMeshImportData->ImportUniformScale = 100;
	TmpFactory->ImportUI->StaticMeshImportData->ImportRotation = FRotator(0.f, 0.f, 270.f);


	TmpImportTask->bReplaceExisting = false;
	TmpImportTask->Factory = TmpFactory;
	TmpImportTask->bAutomated = (!bShowFBXOptionsDialog); // If true avoid Import Dialog window
	// TmpImportTask->bSave = true;
	TmpImportTask->Options = TmpFactory->ImportUI;

	TmpFactory->SetAssetImportTask(TmpImportTask);

	const FString TmpFBXFileName = ObjectTools::SanitizeObjectName(FPaths::GetBaseFilename(FBXFilePath));

	const FString TmpPackageNm = FPaths::Combine(*FBXFileImportDestFolder, *TmpFBXFileName);
	UPackage* TmpAssetPackage = CreatePackage(nullptr, *TmpPackageNm);
	TmpAssetPackage->FullyLoad();

	constexpr EObjectFlags TmpObjFlags = RF_Public | RF_Standalone | RF_Transactional;
	UObject* TmpImportedFBXObject = TmpFactory->ImportObject(TmpFactory->ResolveSupportedClass(), TmpAssetPackage,
	                                                         *TmpFBXFileName, TmpObjFlags, FBXFilePath, nullptr,
	                                                         bIsCancelled);


	const FAssetRegistryModule& AssetRegistryModule = FModuleManager::LoadModuleChecked<FAssetRegistryModule>(
		FName("AssetRegistry"));
	IAssetRegistry& AssetRegistry = AssetRegistryModule.Get();

	// Need to do this if running in the editor with -game to make sure that the assets in the following path are available
	TArray<FString> PathsToScan;
	PathsToScan.Add(FBXFileImportDestFolder);
	AssetRegistry.ScanPathsSynchronous(PathsToScan);

	TArray<FAssetData> MeshAssetList;
	AssetRegistry.GetAssetsByPath(FName(FBXFileImportDestFolder), MeshAssetList);


	TArray<UStaticMesh*> LightObjects;
	TArray<UStaticMesh*> LightSwitch;
	FString SeparatorString = TEXT("_");
	for (int i = 0; i < MeshAssetList.Num(); i++)
	{
		if (MeshAssetList[i].AssetClass != "StaticMesh")
		{
			continue;
		}

		if (UStaticMesh* Asset = static_cast<UStaticMesh*>(MeshAssetList[i].GetAsset()))
		{
			if (IsValid(Asset) && Asset->HasValidRenderData())
			{
				FString AssetName = Asset->GetFName().ToString().Replace(FString::Printf(TEXT("%s%s"), *FileName, *SeparatorString).GetCharArray().GetData(), TEXT(""));
				UE_LOG(LogTemp, Warning, TEXT("Name: %s"), *AssetName);
				if (TSharedPtr<FJsonValue>* MaterialName = Materials.Find(AssetName)) {
					FString Name;
					if(MaterialName->Get()->AsString().Contains("Color"))
					{
						Name = "Material'" + FBXFileImportDestFolder + "/" + MaterialName->Get()->AsString() + "." + MaterialName->Get()->AsString() + "'";
					} else
					{
						Name = "Material'/Game/StarterContent/Materials/" + MaterialName->Get()->AsString() + "." + MaterialName->Get()->AsString() + "'";
					}
					Asset->SetMaterial(0, LoadObject<UMaterial>(nullptr, *Name));
				}
				
				if (Asset->GetName().Contains("LightObject"))
				{
					LightObjects.Add(Asset);
				}
				if (Asset->GetName().Contains("LightSwitch"))
				{
					LightSwitch.Add(Asset);
				}
				Asset->GetBodySetup()->CollisionTraceFlag = CTF_UseSimpleAsComplex;
			}
		}
	}

	for (FLightAndSwitch LightAndSwitch : Lights)
	{
		for (UStaticMesh* LightMesh : LightObjects)
		{
			if (LightMesh->GetName().Contains(LightAndSwitch.lightObject))
			{
				for (UStaticMesh* SwitchMesh : LightSwitch)
				{
					if (SwitchMesh->GetName().Contains(LightAndSwitch.lightSwitch))
					{
						UBuildingAssetData* buildingData = NewObject<UBuildingAssetData>(LightMesh);
						buildingData->ApplyDataToStaticMesh(SwitchMesh, 0);
						LightMesh->AddAssetUserData(buildingData);
					}
				}
			}
		}
	}

	TmpAssetPackage->MarkPackageDirty();
	TmpFactory->SetAssetImportTask(nullptr);
	TmpFactory->CleanUp();
	TmpFactory->RemoveFromRoot();
	UserInt->UpdateUploadText(
		FText::FromString(
			TEXT("Import completed. You can now close this window.\nMeshes are stored in" + PackageName)));
}

TSharedRef<SDockTab> FileImportHandler::ShowImportWindow()
{
	return UserInt->CreateSlateFrontend();
}
