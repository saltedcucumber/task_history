# React-Springboot-App

## Технологии
- ReactJs
- Spring boot

## Запуск react приложения
npm i
npm run start

## Сборка и запуск java 
Сборка: maven clean package 
maven version: 3.8.6
java version: 11

## Task:
Соброать фронт и бэк в docker контейнеры, при сборке java использовать multi stage building.
Написать docker-compose файл с фронтом и бэком + mysql в контейнере, которая при запуске создаёт базу данных фулстек
Передавать данные для подклюения к базе в файл backend/resources/application.propertie используя переменные 
Перед фронтом задеплоить nginx и добавить его тоже в docker compose файл 
mysql version: 8