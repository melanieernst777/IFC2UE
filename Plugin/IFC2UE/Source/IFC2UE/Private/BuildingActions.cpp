#include "BuildingActions.h"
#include "Building.h"

UClass* FBuildingActions::GetSupportedClass() const
{
	return ABuilding::StaticClass();
}

FText FBuildingActions::GetName() const
{
	return INVTEXT("Building");
}

FColor FBuildingActions::GetTypeColor() const
{
	return FColor::Cyan;
}

uint32 FBuildingActions::GetCategories()
{
	return EAssetTypeCategories::Misc;
}

FText FBuildingActions::GetAssetDescription(const FAssetData& AssetData) const
{
	return INVTEXT("Places the static meshes in the current folder");
}

EThumbnailPrimType FBuildingActions::GetDefaultThumbnailPrimitiveType(UObject* Asset) const
{
	return EThumbnailPrimType();
}

FText FBuildingActions::GetDisplayNameFromAssetData(const FAssetData& AssetData) const
{
	return INVTEXT("This is a display name for the building");// FAssetTypeActions_Base::GetDisplayNameFromAssetData(AssetData);
}
