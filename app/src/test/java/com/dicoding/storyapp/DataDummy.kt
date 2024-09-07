package com.dicoding.storyapp

import com.dicoding.storyapp.data.response.ListStoryItem
import com.dicoding.storyapp.data.response.StoryResponse

object DataDummy {

    fun generateDummyResponse(): StoryResponse {
        val items: ArrayList<ListStoryItem> = arrayListOf()
        for (i in 0..20) {
            val story = ListStoryItem(
                photoUrl = i.toString(),
                createdAt = i.toString(),
                name = i.toString(),
                description = i.toString(),
                lon = i.toDouble(),
                id = i.toString(),
                lat = i.toDouble(),
            )
            items.add(story)
        }
        return StoryResponse(
            error = false,
            message = "Stories fetched successfully",
            listStory = items
        )
    }
}