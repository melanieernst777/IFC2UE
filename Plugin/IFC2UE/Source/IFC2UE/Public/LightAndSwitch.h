// Fill out your copyright notice in the Description page of Project Settings.

#pragma once

#include "CoreMinimal.h"

/**
 *
 */
class FLightAndSwitch
{
public:
	FLightAndSwitch();
	FLightAndSwitch(FString LightObject, FString LightSwitch, int Voltage);
	~FLightAndSwitch();

	UPROPERTY(EditAnywhere, BlueprintReadWrite, Category = "Custom Data")
		FString lightObject;

	UPROPERTY(EditAnywhere, BlueprintReadWrite, Category = "Custom Data")
		FString lightSwitch;

	UPROPERTY(EditAnywhere, BlueprintReadWrite, Category = "Custom Data")
		int voltage;
};
