### curl samples (application deployed at application context `topjava/lunchvote`).
> For windows use `Git Bash`

#### get All Users
`curl -s http://localhost:8080/topjava/lunchvote/rest/admin/users --user admin@email.com:xxx111`

#### get Users 100001
`curl -s http://localhost:8080/topjava/lunchvote/rest/admin/users/100001 --user admin@email.com:xxx111`

#### register Users
`curl -s -i -X POST -d "{\"name\":\"New User\",\"email\":\"test@mail.ru\",\"password\":\"test-password\"}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/topjava/lunchvote/rest/profile/register`

#### get Profile
`curl -s http://localhost:8080/topjava/lunchvote/rest/profile --user test@mail.ru:test-password`

#### create invalid User
`curl -s -i -X POST -d "{\"name\":\" \",\"email\":\"admin@email.com\",\"password\":\" \"}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/topjava/lunchvote/rest/admin/users --user admin@email.com:xxx111`

#### get All Restaurants
`curl -s http://localhost:8080/topjava/lunchvote/rest/restaurants --user user1@email.com:xxx222`

#### get Restaurants 100005
`curl -s http://localhost:8080/topjava/lunchvote/rest/restaurants/100005 --user user1@email.com:xxx222`

#### get All Restaurants With Menu
`curl -s http://localhost:8080/topjava/lunchvote/rest/restaurants/with-menu --user user1@email.com:xxx222`

#### create Restaurant
`curl -s -i -X POST -d "{\"name\":\"New Restaurant\",\"description\":\"New Restaurant For Test\"}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/topjava/lunchvote/rest/admin/restaurants --user admin@email.com:xxx111`

#### create invalid Restaurant
`curl -s -i -X POST -d "{\"name\":\"  \",\"description\":\"  \"}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/topjava/lunchvote/rest/admin/restaurants --user admin@email.com:xxx111`

#### create Dish for Restaurant 100005 for 2021-05-02
`curl -s -i -X POST -d "{\"name\":\"New Dish\",\"price\":60054,\"date\":\"2021-05-02\"}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/topjava/lunchvote/rest/admin/restaurants/100005/dishes --user admin@email.com:xxx111`

#### get Menu for Restaurant 100005
`curl -s http://localhost:8080/topjava/lunchvote/rest/restaurants/100005/dishes --user user1@email.com:xxx222`

#### Vote for Restaurant 100005
`curl -s -i -X POST -d "100005" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/topjava/lunchvote/rest/profile/votes --user user1@email.com:xxx222`

#### get Vote for User for Today
`curl -s http://localhost:8080/topjava/lunchvote/rest/profile/votes --user user1@email.com:xxx222`

#### get all Votes of User
`curl -s http://localhost:8080/topjava/lunchvote/rest/profile/votes/all --user user1@email.com:xxx222`

#### cancel the vote of User for today
`curl -s -i -X DELETE http://localhost:8080/topjava/lunchvote/rest/profile/votes --user user1@email.com:xxx222`

#### get all Votes of all Users for any day
`curl -s http://localhost:8080/topjava/lunchvote/rest/admin/votes/byDate?date=2021-03-15 --user admin@email.com:xxx111`