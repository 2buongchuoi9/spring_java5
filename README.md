
table: schools

| STT | FIELD          | TYPE     | NOTE |
| --- | -------------- | -------- | ---- |
| 1   | id             | varchar  |      |
| 2   | school_name    | nvarchar |      |
| 3   | school_adrress | nvarchar |      |
| 4   | school_email   | varchar  |      |
| 5   | school_logo    | varchar  |      |
| 6   | scholl_status | bit  |      |

table: class

| STT | FIELD      | TYPE     | NOTE |
| --- | ---------- | -------- | ---- |
| 1   | id         | varchar  |      |
| 2   | class_name | nvarchar |      |
| 3   | class_code | varchar  |      |
| 4   | class_status | bit  |      |

table: student

| STT | FIELD             | TYPE     | NOTE        |
| --- | ----------------- | -------- | ----------- |
| 1   | id                | varchar  |             |
| 2   | student_name      | varchar  |             |
| 3   | student_class_id  | varchar  | ref(class)  |
| 4   | student_school_id | varchar  | ref(school) |
| 5   | student_birthday  | date     |             |
| 6   | student_img       | varchar  |             |
| 7   | student_adrress   | nvarchar |             |
| 8   | student_email     | varchar  |             |
| 9   | student_phone     | varchar  |             |
| 4   | studen_status | varchar  |      |
