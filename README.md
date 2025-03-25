## calorie-wizard

Сервис позволяет расчитать норму калорий на основе данных о пользователе (пол/рост/вес/возраст) и отслеживать фактическое потребление калорий за день или за период. 

Некоторые особенности сервиса:

1️⃣ Валидация входных данных организована через аннотации (jakarta.validation.constraints + кастомная аннотация для enum);  
2️⃣ В приём пищи пользователь может добавить список из одного и более блюд (меню);  
3️⃣ При повторном добавлении блюда в меню, вес и калории пересчитываются;  
4️⃣ Для оптимизации работы с БД при составлении отчётов используется JPA Projections;  
5️⃣ Юнит-тестирование на JUnit5 + Mockito


Запуск возможен через Docker: docker-compose лежит в корне проекта

Там же находятся тесты Postman (:
#
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
