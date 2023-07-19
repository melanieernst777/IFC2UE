// Fill out your copyright notice in the Description page of Project Settings.

#pragma once

#include "CoreMinimal.h"
#include "AssetTypeActions_Base.h"

/**
 * 
 */
class IFC2UE_API FBuildingActions : public FAssetTypeActions_Base
{
public:
	virtual UClass* GetSupportedClass() const override;
	virtual FText GetName() const override;
	virtual FColor GetTypeColor() const override;
	virtual uint32 GetCategories() override;
	virtual FText GetAssetDescription(const FAssetData& AssetData) const override;
	virtual EThumbnailPrimType GetDefaultThumbnailPrimitiveType(UObject* Asset) const override;
	virtual FText GetDisplayNameFromAssetData(const FAssetData& AssetData) const override;
};
