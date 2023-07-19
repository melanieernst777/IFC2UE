// Fill out your copyright notice in the Description page of Project Settings.

#pragma once

#include "CoreMinimal.h"
#include "HttpModule.h"

/**
 *
 */
class IFC2UE_API UserInterface
{
public:
	UserInterface();
	~UserInterface();
	FReply OnUploadCLicked() const;
	TSharedRef<class SDockTab> CreateSlateFrontend();
	FReply ShowHelp();
	void UpdateUploadText(const FText NewText) const;
	void OpenFileDialog(TArray<FString>& OutFileNames) const;
	TSharedPtr<STextBlock> UploadButtonText;

private:

	TSharedPtr<SButton> UploadButton;
	TSharedPtr<SButton> HelpButton;
	TSharedPtr<STextBlock> HelpButtonText;
	FHttpModule* Http;
};
