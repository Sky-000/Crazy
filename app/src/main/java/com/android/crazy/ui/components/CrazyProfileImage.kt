/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.crazy.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.android.crazy.R

@Composable
fun CrazyProfileImage(
    drawableResource: Int,
    description: String,
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape),
        painter = painterResource(id = drawableResource),
        contentDescription = description,
    )
}

@Composable
fun CrazyProfileImage(
    url : String,
    description: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape),
        model = url,
        contentDescription = description,
    )
}

@Preview
@Composable
fun CrazyProfileImagePreview() {
    Column() {
        CrazyProfileImage(
            drawableResource = R.drawable.avatar_1,
            description = "Crazy",
            modifier = Modifier.size(80.dp)
        )
        CrazyProfileImage(
            url = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg2.doubanio.com%2Fview%2Fphoto%2Fsqs%2Fpublic%2Fp2677102402.jpg&refer=http%3A%2F%2Fimg2.doubanio.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1669874894&t=6ad60b5e5f5de864f682c53ddc03ed3a",
            description = "Crazy",
        )
    }
}
