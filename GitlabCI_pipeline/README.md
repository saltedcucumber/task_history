# DecentWorld Android
- [1. GitFlow](#1-gitflow)
- [2. Архитектура](#2-architecture)
- [3. Структура проекта](#3-project-structure)
- [4. Code Development Rules](#4-code-development-rules)
- [5. Механизм работы с асинхронностью](#5-the-mechanism-of-working-with-asynchrony)
- [6. Абстракции для доступа и реализации источников данных](#6-abstractions-for-accessing-and-implementing-data-sources)
- [7. Абстракции presentation](#7-presentation-abstractions)
- [8. Механизм логирования](#8-logging-mechanism)
- [9. Процесс сборки и отправки приложения](#9-the-process-of-building-and-sending-the-application)
- [10. Подход работы с UI](#10-ui-approach)
- [11. Структура экранов](#11-structure-of-screens)
- [12. Поддержка локализации](#12-localization-support)
- [13. Формат работы со стилями и темами](#13-format-for-working-with-styles-and-themes)
- [14. Схема версионирования приложений](#14-application-versioning-scheme)
- [15. Поддерживаемые девайсы](#15-supported-devices)
## Development Project Rules
### 1. GitFlow
  - Ветки:
    - **master** - продовская ветка приложения. В нее создается pull request из develop, когда все задачи для релиза протестированы и дана отмашка для релиза. Делает это tech lead проекта.
    - **develop** - стабильная ветка приложения с протестированными задачами.
    - **release/[name]** -  ветка релиза определенного скоупа с выполненными фичами.
    - **feature/DWMOB-number-desc** - фича ветка для работы над задачей. После завершения разработки задачи разработчик делает pull request в ветку релиза.
  - Придерживаться формата коммитов описанных в конфлюенсе https://innowise-group.atlassian.net/wiki/spaces/MD/pages/2191884296/GitFlow#Commit-name
  - Файл [.gitignore](https://bitbucket.org/innowise-group/decentworld-android/src/1390440fee6707a37cbd8f0f26939c572e1e1780/.gitignore?at=feature%2FDWMOB-81-android-project-documentation)
### 2. Architecture
- MVP - реализация moxy
- Clean Architecture
- Dependency Injection - Dagger2
### 3. Project structure
  - Packages
    - модуль main
      - [app](app/src/main/java/com/decent/world/app)(Activity, Presenter, View)
        - presentation
          - AppPresenter
          - AppView
          - AppActivity
        - App
      - [core](app/src/main/java/com/decent/world/core) - includes base and global actions, also custom views. 
        - base
          - BaseFragment
          - BasePresenter
          - BaseView
          - ErrorHandler
        - global
          - schedulers
          - ResourceManager
        - view
      - [data](app/src/main/java/com/decent/world/data) - data layer of application. 
        - socket
        - storage
      - [di](app/src/main/java/com/decent/world/di) - settings DI.
        - features
        - module
        - qualifiers
        - subcomponent
        - AppComponent
        - AppInjector
        - FragmentInjector
      - [domain](app/src/main/java/com/decent/world/domain) - domain layer of application.
        - entity
        - interactor
      - [extensions](app/src/main/java/com/decent/world/extensions)
      - [presentation](app/src/main/java/com/decent/world/core) - screens.
- BuildTypes: release, debug
### 4. Code Development Rules 
- Придерживаемся стиля кода описсаного в [стандартах](https://bitbucket.org/innowise-group/mobile-standards/src/master/android/kotlin-best-practices.md?at=master)
- Файл с стилем кода находится в файлу [InnowiseGroup.xml](code_style/InnowiseGroup.xml)
- Для проверки кода используем статического анализатор detekt([инструкция по использованию](https://drive.google.com/file/d/1ele_8sK5h5JIERRJimtWzlWZLI6HW3t9/view?usp=sharing))
### 5. The mechanism of working with asynchrony
RxJava
### 6. Abstractions for accessing and implementing data sources
- Repository
- Interactor
### 7. Presentation abstractions
- [Обработчик ошибок](/app/src/main/java/com/decent/world/core/base/ErrorHandler.kt)
- Навигация с помощью [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started)
- Базовые классы:
  - [BaseFragment](/app/src/main/java/com/decent/world/core/base/BaseFragment.kt)
  - [BasePresenter](/app/src/main/java/com/decent/world/core/base/BasePresenter.kt)
### 8. Logging mechanism
- Remote logging - Firebase (Analytics, Crashlytics)
- Local logging - Terminal / Logcat logging
### 9. The process of building and sending the application
- Билды для тестирования отправляем в Firebase App Distribution и Google Play Testing Internal. Процесс описан в пункте GitFlow
- Стейджи для ci/cd:
    - **Test** - запускается проверка unit tests и lint(свой у каждой платформы).
    - **Analysis** - создается отчет в sonarqube.
    - **Distribute** - отправляется билд в firebase distribution.
    - **Release** - отправляется билд в маркеты: google play internal testing.
    - Работа ci/cd:
        - Стейджи **Test** и **Analysis** запускается на каждой ветке.
        - При создании pull request’а проходят стейдж **Test** и **Analysis**, тем самым давай уверенность ревюеру что билд стабильный и собирается.
        - В проекте есть [файл](version.gradle) с переменными отвечающими за номер версии приложения(majorVersion, minorVersion, patchVersion) и версию кода. После прохождения pull request’а в ветку релиза, patchVersion увеличивается на 1. После прохождения pull request’а в ветку develop, minorVersion увеличивается на 1, patchVersion обновляется до 0. После прохождения pull request’а в ветку master, majorVersion увеличивается на 1, minorVersion и patchVersion обновляется до 0.
        - После прохождения pull request’а в ветку develop запускается стейдж **Distribute** и создается release билд с release окружениями.
        - После прохождения pull request’а в ветку master запускается стейдж **Release** и создается release билд  с release окружениями.
### 10. UI approach
- XML
- ViewBinding
### 11. Structure of screens
- Механизм навигации - Navigation components
- Single activity
### 12. Localization support
- Динамическая (обмен ключом локализации и подгрузка нужного набора лейблов)
будет дополняться
### 13. Format for working with styles and themes
- Шрифт - Montserrat
- Только светлая тема приложения.
- Цветовая схема - будет дополнена позже
- Тема - будет дополнена позже
### 14. Application Versioning Scheme
- min version - 28
- version code - повышается каждый раз при создании билда для гугл маркета
- version number - (major).(minor).(patch) - формат работы описан пункте о работе ci
### 15. Supported devices
Android mobile