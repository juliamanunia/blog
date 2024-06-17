### Description
Simple CRUD application.  
H2 is used as a data storage.

### API
```
GET http://localhost:8080/blog/articles
```
```
GET http://localhost:8080/blog/articles/{id}
```
```
GET http://localhost:8080/blog/articles/before-date?date=yyyy-MM-dd
```
```
POST http://localhost:8080/blog/articles
```
```json
{
  "name": "name1",
  "published": "2024-06-04"
}
```
```
PUT http://localhost:8080/blog/articles/{id}
```
```json
{
  "name": "name2",
  "published": "2024-06-14"
}
```
```
DELETE http://localhost:8080/blog/articles/{id}
```