/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.samples.socialite.ui.photopicker

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.samples.socialite.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoPickerViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val contentResolver: ContentResolver,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val chatIdArg: Long by lazy {
        savedStateHandle.get<Long?>("chatId") ?: throw IllegalArgumentException("chatId is null")
    }

    fun onPhotoPicked(imageUri: Uri) {
        viewModelScope.launch {
            // Ask permission since want to persist media access after app restart too.
            contentResolver.takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

            chatRepository.sendMessage(
                chatId = chatIdArg,
                mediaUri = imageUri.toString(),
                mediaMimeType = contentResolver.getType(imageUri),
                text = "",
            )
        }
    }
}
