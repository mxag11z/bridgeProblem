"""
Integrates del equipo:

Guzmán Dolores Alexis 
Olguin Hernandez Johan Gael
Araujo Galán Maximiliano 
Gathe Esquivel Arleth  Elizabeth
Flores Madrigal Diego

"""

def bridge_recursive(times):
 
    times = sorted(times)  # Ordenamos para facilitar la estrategia

    # Caso base: si solo hay dos o una persona
    if len(times) <= 2:
        # El tiempo será el del más lento
        return times[-1], [f"{times} cruzan -> {times[-1]} min"]

    # Caso base: si quedan tres personas
    if len(times) == 3:
        # Todos cruzan en una secuencia directa
        total = sum(times)
        steps = [
            f"{times[0]} y {times[1]} cruzan -> {times[1]} min",
            f"{times[0]} regresa -> {times[0]} min",
            f"{times[0]} y {times[2]} cruzan -> {times[2]} min"
        ]
        return total, steps

    # --- Recursión ---
    # Estrategia 1:
    # 1) Los dos más rápidos cruzan primero.
    # 2) El más rápido regresa.
    # 3) Los dos más lentos cruzan.
    # 4) El segundo más rápido regresa.
    cost1 = times[0] + 2 * times[1] + times[-1]
    rest_time1, rest_steps1 = bridge_recursive(times[:-2])
    total1 = cost1 + rest_time1
    steps1 = [
        f"{times[0]} y {times[1]} cruzan -> {times[1]} min",
        f"{times[0]} regresa -> {times[0]} min",
        f"{times[-2]} y {times[-1]} cruzan -> {times[-1]} min",
        f"{times[1]} regresa -> {times[1]} min"
    ] + rest_steps1

    # Estrategia 2:
    # 1) El más rápido y el más lento cruzan primero.
    # 2) El más rápido regresa.S
    # 3) El más rápido y el segundo más lento cruzan.
    # 4) El más rápido regresa.
    cost2 = 2 * times[0] + times[-2] + times[-1]
    rest_time2, rest_steps2 = bridge_recursive(times[:-2])
    total2 = cost2 + rest_time2
    steps2 = [
        f"{times[0]} y {times[-1]} cruzan -> {times[-1]} min",
        f"{times[0]} regresa -> {times[0]} min",
        f"{times[0]} y {times[-2]} cruzan -> {times[-2]} min",
        f"{times[0]} regresa -> {times[0]} min"
    ] + rest_steps2

    # Se elige la estrategia con menor tiempo
    if total1 < total2:
        return total1, steps1
    else:
        return total2, steps2


# --- Ejemplo de uso ---
tiempos = [1, 2,5, 8 ]
tiempo_total, pasos = bridge_recursive(tiempos)

print("Tiempo total :", tiempo_total, "min")  # COMENTARIO: Tiempo final óptimo
print("\nPasos del cruce:")
for p in pasos:
    print("-", p)
