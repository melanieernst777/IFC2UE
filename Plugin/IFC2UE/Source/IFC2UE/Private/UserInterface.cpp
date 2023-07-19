// Fill out your copyright notice in the Description page of Project Settings.


#include "UserInterface.h"

#include "Building.h"
#include "ContentBrowserModule.h"
#include "FileImportHandler.h"
#include "IContentBrowserSingleton.h"
#include "IFC2UEStyle.h"
#include "IFC2UECommands.h"
#include "ToolMenus.h"
#include "AssetRegistry/AssetRegistryModule.h"
#include "Framework/Notifications/NotificationManager.h"
#include "MainFrame/Public/Interfaces/IMainFrameModule.h"
#include "Widgets/Notifications/SNotificationList.h"

#define LOCTEXT_NAMESPACE "FIFC2UEModule"

FText UploadIFCFileText;
FText HelpText;
TAttribute<FSlateColor> ButtonColor;
TAttribute<FSlateColor> TextColor;


UserInterface::UserInterface()
{
}

UserInterface::~UserInterface()
{
}


TSharedRef<SDockTab>UserInterface::CreateSlateFrontend()
{
	UploadIFCFileText = LOCTEXT("UploadIFCFile", "Upload IFC File");
	HelpText = LOCTEXT("HelpText", "How can I use this plugin?");
	ButtonColor = FLinearColor(1, 1, 1, 1);
	TextColor = FLinearColor(1, 1, 1, 1);
	Http = &FHttpModule::Get();
	const FText TitleText = LOCTEXT("GameTitle", "IFC2UE");
	const FText InfoText = LOCTEXT(
		"InfoText",
		"With this plugin you can place your building by uploading its IFC file.\nThe building will have materials and will also allow\nyou to switch lights off and on.\n\n\n\n\n\n");

	FSlateFontInfo ButtonTextStyle = FCoreStyle::Get().GetFontStyle("EmbossedText");
	ButtonTextStyle.Size = 17.f;

	FSlateFontInfo HelpTextStyle = FCoreStyle::Get().GetFontStyle("EmbossedText");
	HelpTextStyle.Size = 12.f;

	FSlateFontInfo TitleTextStyle = ButtonTextStyle;
	TitleTextStyle.Size = 30.f;

	FSlateFontInfo TextStyle = ButtonTextStyle;
	TextStyle.Size = 17.f;

	const FMargin ContentPadding = FMargin(50.f, 30.f);
	const FMargin ButtonPadding = FMargin(10.f);


	return SNew(SDockTab)
		.TabRole(NomadTab)
		[
			SNew(SOverlay)
			+ SOverlay::Slot()
		.HAlign(HAlign_Fill)
		.VAlign(VAlign_Fill)
		[
			SNew(SImage)
			.ColorAndOpacity(FColor::Black)
		]
	+ SOverlay::Slot()
		.HAlign(HAlign_Fill)
		.VAlign(VAlign_Fill)
		.Padding(ContentPadding)
		[
			SNew(SVerticalBox)

			// Title text
		+ SVerticalBox::Slot()
		[
			SNew(STextBlock)
			.Font(TitleTextStyle)
		.Text(TitleText)
		.Justification(ETextJustify::Center)
		]

	// Description text
	+ SVerticalBox::Slot()
		[
			SNew(STextBlock)
			.Font(TextStyle)
		.Text(InfoText)
		.Justification(ETextJustify::Center)
		]

	// Upload Button
	+ SVerticalBox::Slot()
		.HAlign(HAlign_Center)
		.VAlign(VAlign_Top)
		.Padding(ButtonPadding)
		[
			SAssignNew(UploadButton, SButton)
			.OnClicked_Raw(this, &UserInterface::OnUploadCLicked)
		.ButtonColorAndOpacity(ButtonColor)
		[
			SAssignNew(UploadButtonText, STextBlock)
			.Font(ButtonTextStyle)
		.Text(UploadIFCFileText)
		.ColorAndOpacity(TextColor)
		.Justification(ETextJustify::Center)
		]
		] + SVerticalBox::Slot()
			.HAlign(HAlign_Center)
			.VAlign(VAlign_Top)
			.Padding(ButtonPadding)
			[
				SAssignNew(HelpButton, SButton)
				.OnClicked_Raw(this, &UserInterface::ShowHelp)
			.ButtonColorAndOpacity(FLinearColor(0, 0, 0, 1))
			[
				SAssignNew(HelpButtonText, STextBlock)
				.Font(HelpTextStyle)
			.Text(HelpText)
			.ColorAndOpacity(TextColor)
			.Justification(ETextJustify::Center)
			]
			]
		]
		];
}

FReply UserInterface::OnUploadCLicked() const
{
	TArray<FString> StrArr;
	OpenFileDialog(StrArr);
	return FReply::Handled();
}

void UserInterface::OpenFileDialog(TArray<FString>& OutFileNames) const
{
	const void* ParentWindowHandle = nullptr;
	const IMainFrameModule& MainFrameModule = FModuleManager::LoadModuleChecked<IMainFrameModule>(TEXT("MainFrame"));
	if (const TSharedPtr<SWindow>& MainFrameParentWindow = MainFrameModule.GetParentWindow(); MainFrameParentWindow.IsValid() && MainFrameParentWindow->GetNativeWindow().IsValid())
	{
		ParentWindowHandle = MainFrameParentWindow->GetNativeWindow()->GetOSWindowHandle();
	}

	if (ParentWindowHandle != nullptr)
	{
		FileImportHandler().OpenFileDialog(OutFileNames, ParentWindowHandle);
		UploadButtonText.Get()->SetColorAndOpacity(FLinearColor(1, 1, 1, 1));
		this->UpdateUploadText(FText::FromString(TEXT("Building up connection to server...")));
		UploadButton.Get()->SetBorderBackgroundColor(FLinearColor(0, 0, 0, 1));
	}
	else
	{
		FNotificationInfo Info(FText::FromString(TEXT("Could not open the file dialog!")));
		Info.ExpireDuration = 5.0f;
		FSlateNotificationManager::Get().AddNotification(Info);
	}
}


FReply UserInterface::ShowHelp()
{
	FNotificationInfo Info(LOCTEXT("SpawnNotificationWithLink_Notification", "Unable to do a thing! Please check the documentation."));
	Info.ExpireDuration = 5.0f;

	//To use an icon in your notifications, add EditorStyle to your Build.cs under PrivateDependencyModuleNames
	Info.bUseSuccessFailIcons = true;

	/*
		Create a Hyperlink that appears at the bottom right of the notification.
		A Hyperlink itself can call a function, in this case we create a small lambda to launch a URL.
		but you could call other functions here.
	*/
	Info.Hyperlink = FSimpleDelegate::CreateLambda([this]() {
		const FString DocsURL = TEXT("https://github.com/melanieernst777/IFC2UE/tree/master/Plugin");
		FPlatformProcess::LaunchURL(*DocsURL, nullptr, nullptr);
		});

	Info.HyperlinkText = LOCTEXT("GoToDocs", "Go to Documentation...");

	FSlateNotificationManager::Get().AddNotification(Info);

	return FReply::Handled();
}

void UserInterface::UpdateUploadText(const FText NewText) const
{
	UploadButtonText->SetText(NewText);
}
