# BukkitIntegrationPlugin
Плагин интеграции Bukkit с личным кабинетом SimpleCabinet  
Для работы нового API пинга сервера праивльно укажите serverName в ServerWrapper(необязательно)  
**Привязка с помощью ServerWrapper обязательна**
## Формат предметов
**Для выдачи предметов с NBT требуется плагин PowerNBT**
- ItemId может принимать строковое значение `minecraft:diamond_sword`/`diamond_sword`/`ic2:itemcoin` или, для старых версий, числовое `minecraft:69`/`1`
- ItemExtra может принимать только числовое значение, не превышающее 65535(short) или `null`
- ItemNbt пишется в формате mojangson, как в `/give`
- Название зачарования пишется в строковом формате `minecraft:luck`/`luck` или числовом. Ограничений на уровень зачарования нет(используется `addUnsafeEnchantement`)  
## Сборка
Перед сборкой скачайте плагин PowerNBT и добавьте его в локальное хранилище maven такой командой:
```
mvn install:install-file \
   -Dfile=./PowerNBT.jar \
   -DgroupId=me.dpohvar.powernbt \
   -DartifactId=PowerNBT \
   -Dversion=0.8.9.2 \
   -Dpackaging=jar \
   -DgeneratePom=true
```
После чего соберите плагин командой `mvn package`
