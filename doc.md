| STT | FIELD          | TYPE     | NOTE |
| --- | -------------- | -------- | ---- |
| 1   | id             | varchar  |      |
| 2   | school_name    | nvarchar |      |
| 3   | school_adrress | nvarchar |      |
| 4   | school_email   | varchar  |      |
| 5   | school_logo    | varchar  |      |

| STT | FIELD      | TYPE     | NOTE |
| --- | ---------- | -------- | ---- |
| 1   | id         | varchar  |      |
| 2   | class_name | nvarchar |      |
| 3   | class_code | varchar  |      |

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
