// Fill out your copyright notice in the Description page of Project Settings.

#pragma once

#include "CoreMinimal.h"
#include "Engine/AssetUserData.h"
#include "BuildingAssetData.generated.h"

/**
 * 
 */
UCLASS()
class IFC2UE_API UBuildingAssetData : public UAssetUserData
{
	GENERATED_BODY()

public:

	UPROPERTY(EditAnywhere, BlueprintReadWrite, AssetRegistrySearchable, Category = "Custom Data")
		UStaticMesh* LightSwitch;


	UPROPERTY(EditAnywhere, BlueprintReadWrite, AssetRegistrySearchable, Category = "Custom Data")
		FString SomeString;

	UPROPERTY(EditAnywhere, BlueprintReadWrite, AssetRegistrySearchable, Category = "Custom Data")
		bool IsLight = false;

	bool ApplyDataToStaticMesh(UStaticMesh* target, int index);
};
