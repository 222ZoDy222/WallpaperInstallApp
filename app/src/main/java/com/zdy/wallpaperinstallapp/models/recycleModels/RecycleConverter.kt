package com.zdy.wallpaperinstallapp.models.recycleModels

import com.zdy.wallpaperinstallapp.inheritance.recycleView.ui.ItemRecycle
import com.zdy.wallpaperinstallapp.db.objectsDB.LocalWallpaper
import com.zdy.wallpaperinstallapp.activity.wallpaperDetails.objectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.api.objectsWeb.RequestImages

class RecycleConverter {

    companion object{
        fun convertImages(requestImages: RequestImages): List<PickUpImage> {
            val resultList = mutableListOf<PickUpImage>()
            for (image in requestImages.items) {
                var desk : String = ""
                for(tag in image.tags){
                    desk += tag.description + "\n"
                }
                desk += "\nRating: ${image.rating}"
                resultList.add(
                    PickUpImage(
                        null,
                        image.image_url,
                        desk)
                )
            }
            return resultList
        }

        fun convertImages(locaWallpapers : List<LocalWallpaper>) : List<ItemRecycle> {

            val resultList = mutableListOf<ItemRecycle>()
            for (image in locaWallpapers) {

                val pickUpImage = PickUpImage(null,
                    image.image_url,
                    image.description,
                    isLiked = true,
                    localID = image.id)

                resultList.add(ItemRecycle.RecycleWallpaperItem(pickUpImage))
            }
            return resultList

        }

    }

}