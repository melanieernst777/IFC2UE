// Copyright Epic Games, Inc. All Rights Reserved.

#include "IFC2UEStyle.h"
#include "Framework/Application/SlateApplication.h"
#include "Styling/SlateStyleRegistry.h"
#include "Slate/SlateGameResources.h"
#include "Interfaces/IPluginManager.h"
#include "Styling/SlateStyleMacros.h"

#define RootToContentDir Style->RootToContentDir

TSharedPtr<FSlateStyleSet> FIFC2UEStyle::StyleInstance = nullptr;

void FIFC2UEStyle::Initialize()
{
	if (!StyleInstance.IsValid())
	{
		StyleInstance = Create();
		FSlateStyleRegistry::RegisterSlateStyle(*StyleInstance);
	}
}

void FIFC2UEStyle::Shutdown()
{
	FSlateStyleRegistry::UnRegisterSlateStyle(*StyleInstance);
	ensure(StyleInstance.IsUnique());
	StyleInstance.Reset();
}

FName FIFC2UEStyle::GetStyleSetName()
{
	static FName StyleSetName(TEXT("IFC2UEStyle"));
	return StyleSetName;
}


const FVector2D Icon16x16(16.0f, 16.0f);
const FVector2D Icon20x20(20.0f, 20.0f);

TSharedRef< FSlateStyleSet > FIFC2UEStyle::Create()
{
	TSharedRef< FSlateStyleSet > Style = MakeShareable(new FSlateStyleSet("IFC2UEStyle"));
	Style->SetContentRoot(IPluginManager::Get().FindPlugin("IFC2UE")->GetBaseDir() / TEXT("Resources"));

	Style->Set("IFC2UE.PluginAction", new IMAGE_BRUSH_SVG(TEXT("icon"), Icon20x20));
	return Style;
}

void FIFC2UEStyle::ReloadTextures()
{
	if (FSlateApplication::IsInitialized())
	{
		FSlateApplication::Get().GetRenderer()->ReloadTextureResources();
	}
}

const ISlateStyle& FIFC2UEStyle::Get()
{
	return *StyleInstance;
}
