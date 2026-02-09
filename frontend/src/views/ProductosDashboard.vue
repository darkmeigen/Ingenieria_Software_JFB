<template>
  <div class="dashboard-container">
    <div class="header">
      <h1>Dashboard de Productos</h1>
      <router-link to="/productos/inventario" class="btn btn-primary">Gestionar Inventario</router-link>
    </div>

    <!-- KPI Cards -->
    <div class="kpi-grid">
      <div class="kpi-card blue">
        <div class="icon-area">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"></path></svg>
        </div>
        <div class="info">
          <h3>Total Productos</h3>
          <p class="value">{{ totalProductos }}</p>
        </div>
      </div>

      <div class="kpi-card orange">
        <div class="icon-area">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"></path><line x1="12" y1="9" x2="12" y2="13"></line><line x1="12" y1="17" x2="12.01" y2="17"></line></svg>
        </div>
        <div class="info">
          <h3>Stock Crítico (< 4)</h3>
          <p class="value">{{ lowStockCount }}</p>
        </div>
      </div>
    </div>

    <!-- Low Stock Table -->
    <div class="section">
      <h2>Stock a Reponer</h2>
      <div class="card table-card">
        <table v-if="lowStockProducts.length > 0">
          <thead>
            <tr>
              <th>Producto</th>
              <th>Descripción</th>
              <th>Stock Actual</th>
              <th>Acción</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="prod in lowStockProducts" :key="prod.id">
              <td>{{ prod.nombre }}</td>
              <td>{{ prod.descripcion }}</td>
              <td class="text-danger font-bold">{{ prod.stock }}</td>
              <td>
                <router-link to="/productos/inventario" class="link-action">Reponer</router-link>
              </td>
            </tr>
          </tbody>
        </table>
        <div v-else class="empty-state">
          <p>Todo el inventario está en niveles óptimos.</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';

const productos = ref([]);

onMounted(async () => {
  try {
    const res = await fetch('http://localhost:8080/api/productos');
    if (res.ok) {
      productos.value = await res.json();
    }
  } catch (e) {
    console.error("Error loading products", e);
  }
});

const totalProductos = computed(() => productos.value.length);
const lowStockProducts = computed(() => productos.value.filter(p => p.stock < 4));
const lowStockCount = computed(() => lowStockProducts.value.length);

</script>

<style scoped>
.dashboard-container { padding: 20px; font-family: 'Segoe UI', sans-serif; }
.header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; }
h1 { color: #333; margin: 0; }
h2 { color: #555; margin-bottom: 15px; font-size: 1.2rem; }

.btn-primary { background: #4CAF50; color: white; border: none; padding: 10px 20px; border-radius: 6px; text-decoration: none; cursor: pointer; transition: background 0.2s; }
.btn-primary:hover { background: #43a047; }

.kpi-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; margin-bottom: 40px; }
.kpi-card { background: white; padding: 20px; border-radius: 10px; box-shadow: 0 4px 6px rgba(0,0,0,0.05); display: flex; align-items: center; gap: 20px; }
.kpi-card .icon-area { width: 60px; height: 60px; border-radius: 12px; display: flex; align-items: center; justify-content: center; }
.kpi-card svg { width: 30px; height: 30px; stroke: white; }

.kpi-card.blue .icon-area { background: linear-gradient(135deg, #2196F3, #1976D2); }
.kpi-card.orange .icon-area { background: linear-gradient(135deg, #FF9800, #F57C00); }

.info h3 { margin: 0 0 5px 0; color: #888; font-size: 0.9rem; font-weight: normal; }
.info .value { margin: 0; color: #333; font-size: 1.8rem; font-weight: bold; }

.table-card { background: white; border-radius: 10px; box-shadow: 0 4px 6px rgba(0,0,0,0.05); overflow: hidden; }
table { width: 100%; border-collapse: collapse; }
th { background: #f9f9f9; text-align: left; padding: 15px 20px; color: #666; font-size: 0.9rem; }
td { padding: 15px 20px; border-bottom: 1px solid #eee; color: #333; }
.text-danger { color: #e74c3c; }
.font-bold { font-weight: bold; }
.link-action { color: #2196F3; text-decoration: none; font-weight: 500; }
.link-action:hover { text-decoration: underline; }

.empty-state { padding: 40px; text-align: center; color: #999; }
</style>
