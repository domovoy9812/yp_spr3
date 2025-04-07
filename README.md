# Учебный проект на 3 спринт
## Общая информация
### ссылка на репозиторий
https://github.com/domovoy9812/yp_spr3.git
### команда для клонирования репозитория
```git clone https://github.com/domovoy9812/yp_spr3.git```
### структура проекта
1. **data** - модуль отвечает за слой доступа к данным, содержит интеграционные тесты
2. **service** - модуль отвечает за слой бизнес-логики
3. **web** - модуль отвечает за слой представления
## Требования для запуска и настройка
### Для работы приложения требуется:
1. Java 21 версии
2. maven для сборки приложения (проверено на версии 3.9.9)
2. запущенный сервер Postgresql 17
+ должна быть создана основная база данных (по умолчанию **bliushtein_yp_sprint3_db**)
+ должен быть создан пользователь для подключения приложения к базе (по умолчанию **bliushtein_yp_sprint3**)
+ Для запуска интеграционных тестов также должна быть создана тестовая база данных (по умолчанию **bliushtein_yp_sprint3_test_db**)
4. запущенный сервер Tomcat (проверено на версии 10.1.39)
### Настройка приложения
1. [application.properties](./web/src/main/resources/application.properties) Основной файл с настройками приложения

| параметр                                    | описание                                                                                                          | значение по умолчанию                                     |
|---------------------------------------------|-------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------|
| spring.datasource.url                       | строка подключения к БД                                                                                           | jdbc:postgresql://localhost:5432/bliushtein_yp_sprint3_db |
| spring.datasource.username                  | имя пользователя для подключения к БД                                                                             | bliushtein_yp_sprint3                                     |
| spring.datasource.password                  | пароль                                                                                                            | 12345                                                     |
| spring.datasource.create-db-tables-on-start | нужно ли создавать необходимые таблицы в БД если они не существуют                                                | true                                                      |
| spring.datasource.drop-db-tables-on-start   | нужно ли удалять старые версии таблиц<br/> (используется только если spring.datasource.create-db-tables-on-start=true) | false                                                     |
2. [test-application.properties](./data/data-impl/src/test/resources/test-application.properties) содержит те же параметры, используется для запуска интеграционных тестов

| параметр                                    | описание                                                                                                               | значение[README.md](README.md) по умолчанию                                     |
|---------------------------------------------|------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------|
| spring.datasource.url                       | строка подключения к тестовой БД                                                                                       | jdbc:postgresql://localhost:5432/bliushtein_yp_sprint3_test_db |
| spring.datasource.username                  | имя пользователя для подключения к тестовой БД                                                                         | bliushtein_yp_sprint3                                     |
| spring.datasource.password                  | пароль                                                                                                                 | 12345                                                     |
| spring.datasource.create-db-tables-on-start | нужно ли создавать необходимые таблицы в БД если они не существуют                                                     | true                                                      |
| spring.datasource.drop-db-tables-on-start   | нужно ли удалять старые версии таблиц<br/> (используется только если spring.datasource.create-db-tables-on-start=true) | true                                                     |
3. Настройки развертывания приложений в Tomcat
+ [pom.xml](./pom.xml) свойство **tomcat.url** - URL сервера Tomcat
+ settings.xml должен содержать секцию ниже, где {user}/{password} имя и пароль пользователя Tomcat с правами на автоматическое развертывание приложений

      <servers>
          <server>
              <id>TomcatServer</id>
              <username>{user}</username>
              <password>{password}</password>
          </server>
      </servers>
### Сборка и запуск
1. Команда для сборки проекта ```mvn clean install```
2. Команда для развертывания приложения на сервер TOMCAT ```mvn tomcat7:deploy```
3. Команда для повторного развертывания приложения на сервера TOMCAT ```mvn tomcat7:redeploy```
4. Команда для удаления приложения с сервера TOMCAT ```mvn tomcat7:undeploy```
5. После успешного развертывания главная страница приложения будет доступна по ссылке <TOMCAT_URL>/bliushtein_sprint3/feed