// Fill out your copyright notice in the Description page of Project Settings.


#include "BuildingAssetData.h"

bool UBuildingAssetData::ApplyDataToStaticMesh(UStaticMesh* target, int index)
{
	IsLight = true;
	LightSwitch = target;
	return true;
}
