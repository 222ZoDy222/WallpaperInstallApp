package com.zdy.wallpaperinstallapp.wallpapersList.RecycleView.Model

import com.zdy.wallpaperinstallapp.wallpapersList.RecycleView.UI.ItemRecycle
import com.zdy.wallpaperinstallapp.models.ObjectsDB.LocalWallpaper
import com.zdy.wallpaperinstallapp.models.ObjectsUI.PickUpImage
import com.zdy.wallpaperinstallapp.models.Web.ObjectsWeb.RequestImages

class RecycleConverter {

    companion object{
        fun ConvertImages(requestImages: RequestImages): List<PickUpImage> {
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
                        desk))
            }
            return resultList
        }

        fun ConvertImages(locaWallpapers : List<LocalWallpaper>) : List<ItemRecycle> {

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