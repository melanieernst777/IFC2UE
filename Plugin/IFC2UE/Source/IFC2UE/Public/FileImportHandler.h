// Fill out your copyright notice in the Description page of Project Settings.

#pragma once
#include "Http.h"
#include "LightAndSwitch.h"
#include "CoreMinimal.h"

class FIFC2UEModule;
/**
 *
 */
class IFC2UE_API FileImportHandler
{
public:
	FileImportHandler();
	~FileImportHandler();

	void OpenFileDialog(TArray<FString>& OutFileNames, const void* ParentWindowHandle);

	void GetFileAndInformation(FHttpRequestPtr Request, FHttpResponsePtr Response, bool bSuccessful) const;

	auto FBXFileImport(FString FileName, TArray<FLightAndSwitch> Lights,
	                   TMap<FString, TSharedPtr<FJsonValue>> Materials) const -> void;

	static TSharedRef<class SDockTab> ShowImportWindow();
};
