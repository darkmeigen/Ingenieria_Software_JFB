<template>
  <div class="dashboard-container">
    <div class="header">
      <h1>Inicio > Ventas</h1>
      <div class="company-info">
        <span>Mantenimiento {{loggedUserName}}</span>
        <!--<small>{{ loggedUserName }}</small>-->
      </div>
    </div>

    <!-- Indicadores -->
    <h2>Indicadores</h2>
    <div class="stats-grid">
      <div class="stat-card blue">
        <div class="content">
          <h3>Total:</h3>
          <p class="amount">$ {{ stats.totalVentas?.toFixed(2) }}</p>
        </div>
        <div class="icon">
            <!-- REQUESTED DOLLAR ICON -->
            <svg viewBox="0 0 24 24" fill="none" class="svg-icon-white">
                <path d="M12 1v22M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
        </div>
      </div>
      <div class="stat-card yellow">
        <div class="content">
          <h3>SubTotal:</h3>
          <p class="amount">$ {{ stats.subTotal?.toFixed(2) }}</p>
        </div>
        <div class="icon">
             <svg viewBox="0 0 24 24" fill="none" class="svg-icon-dark">
                <path d="M12 1v22M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6" stroke="#333" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
        </div>
      </div>
      <div class="stat-card green">
        <div class="content">
          <h3>Impuestos:</h3>
          <p class="amount">$ {{ stats.impuestos?.toFixed(2) }}</p>
        </div>
        <div class="icon">
             <!-- Placeholder SVG for now, will update if icon file found -->
             <svg viewBox="0 0 24 24" fill="none" class="svg-icon-white">
                <path d="M12 2v20M2 12h20" stroke="white" stroke-width="2" stroke-linecap="round"/>
             </svg>
        </div>
      </div>
    </div>

    <!-- Charts Section -->
    <div class="charts-row">
      <div class="chart-container">
        <h3>Ventas últimos 6 meses</h3>
        <div class="chart-content">
             <!-- Simple SVG Line Chart -->
             <svg viewBox="0 0 500 200" class="svg-chart">
                <!-- Grid Lines -->
                <line x1="50" y1="150" x2="480" y2="150" stroke="#eee" />
                <line x1="50" y1="100" x2="480" y2="100" stroke="#eee" />
                <line x1="50" y1="50" x2="480" y2="50" stroke="#eee" />
                
                <!-- Data Path -->
                <polyline 
                    fill="none" 
                    stroke="#3498db" 
                    stroke-width="3"
                    :points="chartPoints"
                />
                <!-- Points -->
                 <circle v-for="(point, i) in stats.salesLast6Months" :key="i"
                    :cx="50 + (i * 80)"
                    :cy="200 - (point.total / maxSales * 150) - 20" 
                    r="4" fill="#3498db"
                 />
                 <!-- Labels X -->
                 <text v-for="(point, i) in stats.salesLast6Months" :key="'label'+i"
                    :x="50 + (i * 80)" y="190" font-size="10" text-anchor="middle" fill="#666">
                    {{ point.month.substring(0,3) }}
                 </text>
             </svg>
        </div>
      </div>
      
      <div class="chart-container">
        <h3>Transacciones último mes</h3>
         <div class="chart-content">
             <!-- Placeholder Area Chart -->
             <svg viewBox="0 0 500 200" class="svg-chart">
                <path d="M50,180 L100,20 L200,20 L300,100 L400,150 L450,180 Z" fill="rgba(144, 238, 144, 0.2)" stroke="#2ecc71" stroke-width="2"/>
             </svg>
        </div>
      </div>
    </div>

    <!-- Tables Row -->
    <div class="tables-row">
      <!-- Top Clientes -->
      <div class="table-card">
        <h3>Top Clientes</h3>
        <table>
          <thead>
            <tr>
              <th>Identificación</th>
              <th>Cliente</th>
              <th>Teléfono</th>
              <th>Ventas</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="client in stats.topClients" :key="client.identificacion">
              <td>{{ client.identificacion }}</td>
              <td>{{ client.nombre }}</td>
              <td>{{ client.telefono }}</td>
              <td>{{ client.totalCompras.toFixed(2) }}</td>
            </tr>
            <tr v-if="!stats.topClients?.length"><td colspan="4">No hay datos</td></tr>
          </tbody>
        </table>
      </div>

      <!-- Lo más vendido -->
      <div class="table-card">
        <h3>Lo más vendido</h3>
        <table>
          <thead>
            <tr>
              <th>Producto</th>
              <th>Categoría</th>
              <th>Cant.</th>
              <th>Ventas</th>
            </tr>
          </thead>
          <tbody>
             <tr v-for="prod in stats.topProducts" :key="prod.nombre">
              <td>{{ prod.nombre }}</td>
              <td>{{ prod.categoria }}</td>
              <td>{{ prod.cantidad }}</td>
              <td>{{ prod.totalVentas.toFixed(2) }}</td>
            </tr>
            <tr v-if="!stats.topProducts?.length"><td colspan="4">No hay datos</td></tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';

const stats = ref({
    totalVentas: 0,
    subTotal: 0,
    impuestos: 0,
    notasCredito: 0,
    salesLast6Months: [],
    topClients: [],
    topProducts: []
});

const loggedUserName = ref('USUARIO');

const maxSales = computed(() => {
    if (!stats.value.salesLast6Months?.length) return 1000;
    return Math.max(...stats.value.salesLast6Months.map(s => s.total)) || 1000;
});

const chartPoints = computed(() => {
    if (!stats.value.salesLast6Months?.length) return "";
    return stats.value.salesLast6Months.map((point, i) => {
        const x = 50 + (i * 80);
        const y = 200 - (point.total / maxSales.value * 150) - 20;
        return `${x},${y}`;
    }).join(" ");
});

onMounted(async () => {
    try {
        const userStr = localStorage.getItem('user');
        if (userStr) {
             const u = JSON.parse(userStr);
             loggedUserName.value = u.nombre || u.username || 'USUARIO';
        }
        
        const res = await fetch('http://localhost:8080/api/stats/dashboard');
        if (res.ok) {
            stats.value = await res.json();
        }
    } catch (e) {
        console.error("Error fetching stats");
    }
});
</script>

<style scoped>
.dashboard-container { 
    padding: 20px; 
    min-height: 100vh;
    /* Gradient: White to Navy Blue */
    background: linear-gradient(180deg, #ffffff 55%, #001f3f 100%);
}
.header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.company-info { text-align: right; }
.company-info span { font-weight: bold; color: #666; display: block; }
.company-info small { color: #999; }

h2, h3 { color: #444; margin-bottom: 15px; }

/* Stats Grid - Adjusted for 3 columns since one is removed */
.stats-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; margin-bottom: 30px; }
.stat-card {
    padding: 20px; border-radius: 8px; color: white;
    display: flex; justify-content: space-between; align-items: center;
    box-shadow: 0 4px 6px rgba(0,0,0,0.1);
}
.stat-card.blue { background: #2196F3; }
.stat-card.yellow { background: #FFC107; color: #333; }
.stat-card.green { background: #4CAF50; }
/* Red card removed */
.content h3 { font-size: 0.9rem; margin: 0; color: inherit; opacity: 0.9; }
.amount { font-size: 1.5rem; font-weight: bold; margin: 5px 0 0 0; }
.svg-icon-white { width: 32px; height: 32px; }
.svg-icon-dark { width: 32px; height: 32px; }

/* Charts */
.charts-row { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-bottom: 30px; }
.chart-container { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); }
.svg-chart { width: 100%; height: 200px; }

/* Tables */
.tables-row { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; }
.table-card { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); }
table { width: 100%; border-collapse: collapse; font-size: 0.9rem; }
th { text-align: left; color: #666; border-bottom: 2px solid #eee; padding: 10px; }
td { padding: 10px; border-bottom: 1px solid #eee; color: #333; }
</style>
