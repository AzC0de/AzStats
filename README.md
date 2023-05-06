# AzStats

Un plugin de estadísticas para jugadores en Minecraft, compatible con PlaceholderAPI.

## Características

- Estadísticas de jugadores para:
  - Kills
  - MobKills
  - Deaths
  - PlayTime
- Rangos diarios, semanales, mensuales y de todos los tiempos
- Integración con PlaceholderAPI

## Requisitos

- Servidor de Minecraft (versión compatible con el plugin)
- [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)

## Instalación

1. Descargue el archivo JAR del plugin y colóquelo en la carpeta 'plugins' de su servidor.
2. Reinicie su servidor para cargar el plugin.
3. Edite el archivo de configuración `config.yml` en la carpeta 'plugins/AzStats' para ajustar la configuración del plugin según sus necesidades.
4. Reinicie su servidor nuevamente para aplicar los cambios de configuración.

## Uso

Para utilizar las estadísticas en su servidor, utilice los marcadores de posición de PlaceholderAPI con el siguiente formato:

%playerstats_<stat>_%


- `<stat>` puede ser: `kills`, `mobkills`, `deaths` o `playtime`
- `<rank>` es el número de rango que desea mostrar
- `<timeRange>` puede ser: `daily`, `weekly`, `monthly` o `alltime`

Ejemplo:

%playerstats_kills_1_weekly%


Este marcador de posición mostrará el nombre del jugador con el mayor número de kills en la última semana.

