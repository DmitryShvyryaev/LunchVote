curl http://localhost:8080/topjava/lunchvote/rest/restaurants - get all restaurants
curl http://localhost:8080/topjava/lunchvote/rest/restaurants/100005 - get simple restaurant
curl http://localhost:8080/topjava/lunchvote/rest/restaurants/with-menu - get all restaurants with menu for today
curl http://localhost:8080/topjava/lunchvote/rest/restaurants/100005/with-menu - get the simple restaurant with a menu for today
C:\Users\Inver>curl -i -X POST -H "Content-Type: application/json" -d "{\"name\":\"newRestaurant\",\"description\":\"newDescriptionForNewRestaurant\"}" http://localhost:8080/topjava/lunchvote/rest/restaurants - create new restaurant 