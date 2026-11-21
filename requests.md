# Try it

All commands assume the app is running on localhost:8080.

## List employees (paginated)
curl "http://localhost:8080/api/employees?page=0&size=5"

## Create an employee
curl -X POST http://localhost:8080/api/employees \
-H "Content-Type: application/json" \
-d '{"name":"New Person","email":"new@example.com","department":"IT"}'

## Assign equipment to someone
curl -X POST http://localhost:8080/api/assignments \
-H "Content-Type: application/json" \
-d '{"employeeId":1,"equipmentId":2}'

## Who has what right now
curl "http://localhost:8080/api/assignments/active"

## Return it (use the id from the assign response)
curl -X PUT http://localhost:8080/api/assignments/5/return

## Reports
curl http://localhost:8080/api/equipment/report/by-status
curl http://localhost:8080/api/assignments/report/active-by-employee