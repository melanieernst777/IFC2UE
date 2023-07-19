// Fill out your copyright notice in the Description page of Project Settings.

#pragma once

#include "CoreMinimal.h"
#include "Components/PointLightComponent.h"
#include "GameFramework/Actor.h"
#include "Building.generated.h"

class USphereComponent;
/**
 * 
 */
UCLASS(BlueprintType, Blueprintable, NonTransient, config = Engine, meta = (ShortTooltip = "Place the this asset in the same folder as your building assets to place the building as a whole.", IsBlueprintBase = "true"))
class IFC2UE_API ABuilding : public AActor
{
	GENERATED_BODY()
public:
	void Init();
	ABuilding();
	virtual void PostInitProperties() override;

	UPROPERTY(BlueprintReadWrite, EditAnywhere, AssetRegistrySearchable, Category = Building, DisplayName = "Path to the building assets")
		FString PathToMeshes;

	float LightIntensity;

	UPROPERTY()
	TMap<USphereComponent*, UPointLightComponent*> LightsAndSwitches;

	// declare overlap begin function
	UFUNCTION()
		void OnOverlapBegin(class UPrimitiveComponent* OverlappedComp, class AActor* OtherActor, class UPrimitiveComponent* OtherComp, int32 OtherBodyIndex, bool bFromSweep, const FHitResult& SweepResult);

	// declare overlap end function
	UFUNCTION()
		void OnOverlapEnd(class UPrimitiveComponent* OverlappedComp, class AActor* OtherActor, class UPrimitiveComponent* OtherComp, int32 OtherBodyIndex);

	UFUNCTION()
		void ComponentClicked(UPrimitiveComponent* PrimitiveComponent, FKey Key);

	UFUNCTION()
		void AddLightToComponent(UStaticMeshComponent* Component, FVector LightLocation, FVector SphereLocation);

	virtual void PostEditChangeProperty(FPropertyChangedEvent& PropertyChangedEvent) override;

	UPROPERTY(EditAnywhere, BlueprintReadOnly, AssetRegistrySearchable, Category = Max)
		int32 MaxCount;
	
	UPROPERTY(EditAnywhere, Category = "IFC2UE")
		USceneComponent* OurVisibleComponent;

	UPROPERTY(EditAnywhere, Category = "IFC2UE")
		UPrimitiveComponent* CurrentLightComponent;

	void ToggleLight(UPointLightComponent* LightComponent) const;

	virtual void BeginPlay() override;
	virtual void PostEditMove(bool bFinished) override;

private:
	UPROPERTY(EditAnywhere, meta = (AllowPrivateAccess = "true"), Category = "IFC2UE")
		TSubclassOf<AActor> ActorToSpawn;
};
