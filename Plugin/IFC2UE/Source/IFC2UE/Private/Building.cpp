// Fill out your copyright notice in the Description page of Project Settings.

#include "Building.h"

#include "BuildingAssetData.h"
#include "DrawDebugHelpers.h"
#include "EngineUtils.h"
#include "Components/PointLightComponent.h"
#include "Components/SphereComponent.h"
#include "AssetRegistry/AssetRegistryModule.h"
#include "Framework/Notifications/NotificationManager.h"
#include "Widgets/Notifications/SNotificationList.h"

class UBuildingAssetData;

bool GHasBeenInitialized;


void ABuilding::Init()
{
	if (GHasBeenInitialized) return;
	if (GetWorld() == nullptr)
	{
		return;
	}
	// PrimaryActorTick.bCanEverTick = true;

	OurVisibleComponent = NewObject<UStaticMeshComponent>(this, UStaticMeshComponent::StaticClass(), FName("Visible Component"));
	OurVisibleComponent->RegisterComponent();
	SetRootComponent(OurVisibleComponent);
	const FAssetRegistryModule& AssetRegistryModule = FModuleManager::LoadModuleChecked<FAssetRegistryModule>(FName("AssetRegistry"));
	IAssetRegistry& AssetRegistry = AssetRegistryModule.Get();
	TArray<FString> PathsToScan;
	FString Path = PathToMeshes;
	PathsToScan.Add(Path);
	AssetRegistry.ScanPathsSynchronous(PathsToScan);
	TArray<FAssetData> MeshAssetList;
	AssetRegistry.GetAssetsByPath(FName(Path), MeshAssetList);

	if (MeshAssetList.Num() == 0)
	{
		FNotificationInfo Info(FText::FromString("There are no meshes in the defined path.\nMake sure that the path is correct.\nThe path can be changed by double-clicking the asset and changing the \"path to the building\""));
		Info.ExpireDuration = 10.0f;
		FSlateNotificationManager::Get().AddNotification(Info);
		return;
	}

	for (int i = 0; i < MeshAssetList.Num(); i++)
	{
		if (MeshAssetList[i].AssetClass != "StaticMesh") continue;
		UStaticMesh* Mesh = static_cast<UStaticMesh*>(MeshAssetList[i].GetAsset());
		UStaticMeshComponent* MyPtr = NewObject<UStaticMeshComponent>(this, UStaticMeshComponent::StaticClass(), MeshAssetList[i].AssetName);
		MyPtr->Mobility = EComponentMobility::Movable;
		MyPtr->RegisterComponent();

		MyPtr->SetStaticMesh(Mesh);
		MyPtr->AttachToComponent(GetRootComponent(), FAttachmentTransformRules::KeepRelativeTransform);
		MyPtr->SetStaticMesh(Mesh);

		if(UAssetUserData* ExistingData = Mesh->GetAssetUserDataOfClass(UBuildingAssetData::StaticClass()); ExistingData != nullptr)
		{
			const UBuildingAssetData* Data = Cast<UBuildingAssetData>(ExistingData);
			FVector SphereLocation;
			if(Data->LightSwitch)
			{
				SphereLocation = Data->LightSwitch->GetBoundingBox().GetCenter();
			}
			const FVector LightVector = Mesh->GetBoundingBox().GetCenter();
			AddLightToComponent(MyPtr, LightVector, SphereLocation);
		}
		if (Mesh->GetPathName().Contains("Door"))
		{
			MyPtr->SetCollisionEnabled(ECollisionEnabled::NoCollision);
		}
	}
	GHasBeenInitialized = GHasBeenInitialized || (MeshAssetList.Num() != 0);
}

ABuilding::ABuilding(){}

void ABuilding::PostInitProperties()
{
	Super::PostInitProperties();
	Init();
}


void ABuilding::ToggleLight(UPointLightComponent* LightComponent) const
{
	if (CurrentLightComponent != nullptr)
	{
		LightComponent->ToggleVisibility();
	}
}

void ABuilding::BeginPlay()
{
	Super::BeginPlay();
}

void ABuilding::PostEditMove(bool bFinished)
{
	GHasBeenInitialized = false;
	Super::PostEditMove(bFinished);
}


void ABuilding::OnOverlapBegin(class UPrimitiveComponent* OverlappedComp, class AActor* OtherActor,
                               class UPrimitiveComponent* OtherComp, int32 OtherBodyIndex, bool bFromSweep,
                               const FHitResult& SweepResult)
{
	if (OtherActor && (OtherActor != this) && OtherComp)
	{
		CurrentLightComponent = OverlappedComp;
		const TMap<USphereComponent*, UPointLightComponent*>::ValueInitType LightComponent = LightsAndSwitches.FindOrAdd(
			static_cast<USphereComponent*>(CurrentLightComponent));
		if (LightComponent != nullptr)
		{
			ToggleLight(LightComponent);
			if (LightComponent->IsVisible())
			{
				GEngine->AddOnScreenDebugMessage(-1, 5.f, FColor::Blue, TEXT("The light is turned on now"));
			}
			else
			{
				GEngine->AddOnScreenDebugMessage(-1, 5.f, FColor::Blue, TEXT("The light is turned off now"));
			}
		}
	}
}

void ABuilding::OnOverlapEnd(class UPrimitiveComponent* OverlappedComp, class AActor* OtherActor,
	class UPrimitiveComponent* OtherComp, int32 OtherBodyIndex)
{
	if (OtherActor && (OtherActor != this) && OtherComp)
	{
		CurrentLightComponent = nullptr;
	}
}

void ABuilding::ComponentClicked(UPrimitiveComponent* PrimitiveComponent, FKey Key)
{
	if (PrimitiveComponent != nullptr)
	{
		const TMap<USphereComponent*, UPointLightComponent*>::ValueInitType LightComponent = LightsAndSwitches.
			FindOrAdd(static_cast<USphereComponent*>(PrimitiveComponent));
		if (LightComponent != nullptr)
		{
			LightComponent->ToggleVisibility();
		}
	}
}

void ABuilding::AddLightToComponent(UStaticMeshComponent* Component, FVector LightLocation, FVector SphereLocation)
{
	LightIntensity = 1000.0f;
	UPointLightComponent* light = NewObject<UPointLightComponent>(this, UPointLightComponent::StaticClass());
	light->RegisterComponent();
	light->Intensity = LightIntensity;
	light->bAddedToSceneVisible = true;
	light->AttachToComponent(OurVisibleComponent, FAttachmentTransformRules::KeepRelativeTransform);
	Component->SetCastShadow(false);
	light->SetVisibility(true);
	light->SetLightColor(FLinearColor(255, 244, 173));
	light->SetRelativeLocation(LightLocation);

	if(!SphereLocation.IsZero())
	{
		USphereComponent* LightSphere = NewObject<USphereComponent>(this, USphereComponent::StaticClass());
		LightSphere->RegisterComponent();
		LightSphere->InitSphereRadius(20.0f);
		LightSphere->SetCollisionProfileName(TEXT("Trigger"));
		LightSphere->AttachToComponent(GetRootComponent(), FAttachmentTransformRules::KeepRelativeTransform);
		LightSphere->OnComponentBeginOverlap.AddDynamic(this, &ABuilding::OnOverlapBegin);
		LightSphere->OnComponentEndOverlap.AddDynamic(this, &ABuilding::OnOverlapEnd);
		Component->OnClicked.AddDynamic(this, &ABuilding::ComponentClicked);
		LightSphere->SetRelativeLocation(SphereLocation);
		LightsAndSwitches.Add(LightSphere, light);
		DrawDebugPoint(GetWorld(), SphereLocation, 20, FColor(255, 0, 255), false);
	}
}

void ABuilding::PostEditChangeProperty(FPropertyChangedEvent& PropertyChangedEvent)
{
	Super::PostEditChangeProperty(PropertyChangedEvent);
}
