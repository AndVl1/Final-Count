# Final Count

## Планирование и распределение финансов

### Состав команды
- [Андрей Владиславов](https://github.com/AndVl1)
- [Никита Лобаев](https://github.com/NikitaLobaev)
---
### Основная идея приложения
Android-приложение для учёта расходов:

----
### Используемые функции

1. OAuth2 авторизация
2. Архитектура - MVP
3. Сохранение данных в SQLite базе данных, интеграция с firebase
 1.  для взаимодействия с базой данных испоьлзуется библиотека Room
 2. Для показа списка покупок используется Paging Library
4. Окно общего счёта расходов
 1. Отображаются даты начала, конца периода, количество оставшихся денег по плану и количество потраченных денег
 2. Хранение и отображение аналогично покупкам
5. Тестирование (UI тесты с использованием Espresso)
6. Синхронизация между устройствами, если с одного аккаунта выполнен логин в несколько девайсов


---

#### Скриншоты

[![Авторизация](screenshots/auth.png?raw=true "Авторизация")](https://github.com/AndVl1/Final-Count/blob/master/screenshots/auth.png "Скриншоты")
[![Список покупок](screenshots/purchaseslist.png?raw=true "Список покупок")](https://github.com/AndVl1/Final-Count/blob/master/screenshots/purchaseslist.png "Список покупок")
[![Добавление новой покупки](screenshots/new_purchase.png?raw=true "Добавление новой покупки")](https://github.com/AndVl1/Final-Count/blob/master/screenshots/new_purchase.png "Добавление новой покупки")
[![Добавление нового плана](screenshots/new_plan.png?raw=true "Добавление нового плана")](https://github.com/AndVl1/Final-Count/blob/master/screenshots/new_purchase.png "Добавление нового плана")
[![Пинкод](screenshots/new_purchase.png?raw=true "Пинкод")](https://github.com/AndVl1/Final-Count/blob/master/screenshots/pincode-screenshot.png "пинкод")
---
#### Примеры работы приложения

[Примеры работы приложения](https://github.com/AndVl1/Final-Count/blob/master/samples)
