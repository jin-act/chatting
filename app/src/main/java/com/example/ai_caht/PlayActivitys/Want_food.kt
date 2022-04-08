package com.example.ai_caht.PlayActivitys

object Want_food {
    fun want_food(num : Int) : String{
        var food  = ""
        if(num == 1){
            food = "피자"
        }else if(num == 2){
            food = "포테이토피자"
        }else if(num == 3){
            food = "고구마피자"
        }else if(num == 4){
            food = "불고기피자"
        }else if(num == 5){
            food = "치즈피자"
        }else if(num == 6){
            food = "페퍼로니피자"
        }else if(num == 7){
            food = "닭안심살피자"
        }else if(num == 8){
            food = "멕시칸바이트피자"
        }else if(num == 9){
            food = "콤비네이션피자"
        }else if(num == 10){
            food = "스테이크피자"
        }
        return food
    }
}