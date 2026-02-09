<template>
  <div class="historico-container">
    <div class="top-bar">
        <span>Inicio > Ventas > Histórico</span>
        <div class="company-header">
            <span>Mantenimiento</span>
            <small>{{ loggedUserName }}</small>
        </div>
    </div>

    <div class="card">
      <div class="card-header">
        <h2>Historial de Facturas Emitidas</h2>
        <div class="filters">
            <input type="date" v-model="filters.fechaInicio" class="form-input">
            <input type="date" v-model="filters.fechaFin" class="form-input">
            <button class="btn btn-primary" @click="filtrar">Filtrar</button>
        </div>
      </div>

      <table class="data-table">
        <thead>
          <tr>
            <th>No. Factura</th>
            <th>Fecha</th>
            <th>Cliente</th>
            <th>Identificación</th>
            <th>Total</th>
            <th>Estado</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="factura in displayedFacturas" :key="factura.id">
            <td>{{ factura.id.substring(0, 8) }}...</td>
            <td>{{ formatDate(factura.fecha) }}</td>
            <td>{{ factura.cliente?.nombre || 'N/A' }}</td>
            <td>{{ factura.cliente?.identificacion || 'N/A' }}</td>
            <td>$ {{ factura.total?.toFixed(2) }}</td>
            <td>
                <span :class="['badge', factura.estado === 'CREADA' ? 'success' : 'warning']">
                    {{ factura.estado }}
                </span>
            </td>
            <td>
                <button class="btn-icon" title="Ver Detalle" @click="verFactura(factura)">
                     <svg viewBox="0 0 24 24" fill="none" class="icon-blue" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle></svg>
                </button>
                 <button class="btn-icon" title="Imprimir PDF" @click="imprimirFactura(factura.id)">
                     <svg viewBox="0 0 24 24" fill="none" class="icon-dark" stroke-width="2"><polyline points="6 9 6 2 18 2 18 9"></polyline><path d="M6 18H4a2 2 0 0 1-2-2v-5a2 2 0 0 1 2-2h16a2 2 0 0 1 2 2v5a2 2 0 0 1-2 2h-2"></path><rect x="6" y="14" width="12" height="8"></rect></svg>
                </button>
            </td>
          </tr>
          <tr v-if="displayedFacturas.length === 0">
            <td colspan="7" class="empty">No se encontraron facturas.</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- INVOICE DETAIL MODAL -->
    <div v-if="selectedFactura" class="modal-overlay" @click.self="selectedFactura = null">
        <div class="modal-content invoice-modal animate-pop">
            <div class="modal-header">
                <h3>Factura #{{ selectedFactura.id }}</h3>
                <span class="close-btn" @click="selectedFactura = null">×</span>
            </div>
            <div class="modal-body invoice-body">
                 <!-- Header -->
                 <div class="inv-header">
                    <div class="logo-area">
                        <!-- Logo -->
                        <div class="circle-logo">
                            <img src="/AJM.jpeg" alt="AJM Logo" style="width:100%; height:100%; object-fit:cover; border-radius:50%;" />
                        </div>
                    </div>
                    <div class="inv-title">
                        <h1>FACTURA</h1>
                    </div>
                 </div>

                 <!-- Info -->
                 <div class="inv-info">
                    <p><strong>Factura nº:</strong> {{ selectedFactura.id }}</p>
                    <p><strong>Fecha:</strong> {{ formatDate(selectedFactura.fecha) }}</p>
                    <p><strong>Cliente:</strong> {{ selectedFactura.cliente?.nombre }}</p>
                 </div>

                 <!-- Table -->
                 <table class="inv-table">
                    <thead>
                        <tr class="header-row">
                            <th>Descripción</th>
                            <th>Cantidad</th>
                            <th>Precio</th>
                            <th>Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(det, i) in selectedFactura.detalles" :key="i">
                            <td>{{ det.producto.nombre }}</td>
                            <td class="text-center">{{ det.cantidad }}</td>
                            <td class="text-right">$ {{ det.precioUnitario?.toFixed(2) }}</td>
                            <td class="text-right">$ {{ det.subtotal?.toFixed(2) }}</td>
                        </tr>
                    </tbody>
                 </table>

                 <!-- Totals -->
                 <div class="inv-totals">
                    <div class="total-row"><span>Subtotal</span> <span>$ {{ selectedFactura.subtotal?.toFixed(2) }}</span></div>
                    <div class="total-row"><span>Impuestos</span> <span>$ {{ selectedFactura.iva?.toFixed(2) }}</span></div>
                    <div class="total-row big"><span>TOTAL</span> <span>$ {{ selectedFactura.total?.toFixed(2) }}</span></div>
                 </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" @click="selectedFactura = null">Cerrar</button>
                <button class="btn btn-primary" @click="imprimirFactura(selectedFactura.id)">Descargar PDF</button>
            </div>
        </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';

const facturas = ref([]);
const loggedUserName = ref('USUARIO');
const selectedFactura = ref(null);
const filters = ref({
    fechaInicio: '',
    fechaFin: ''
});

onMounted(async () => {
    const userStr = localStorage.getItem('user');
    if (userStr) {
        const u = JSON.parse(userStr);
        loggedUserName.value = u.nombre || u.username || 'USUARIO';
    }
    await loadFacturas();
});

const loadFacturas = async () => {
    try {
        const res = await fetch('http://localhost:8080/api/facturas');
        if (res.ok) {
            facturas.value = await res.json();
            facturas.value.sort((a, b) => new Date(b.fecha) - new Date(a.fecha));
        }
    } catch (e) {
        console.error("Error loading invoices");
    }
};

const displayedFacturas = computed(() => {
    let data = facturas.value;
    if (filters.value.fechaInicio) {
        data = data.filter(f => f.fecha >= filters.value.fechaInicio);
    }
    if (filters.value.fechaFin) {
        data = data.filter(f => f.fecha <= filters.value.fechaFin);
    }
    return data;
});

const formatDate = (dateStr) => {
    if(!dateStr) return '';
    return new Date(dateStr).toLocaleDateString('es-ES', { year: 'numeric', month: 'long', day: 'numeric' });
};

const verFactura = (factura) => {
    selectedFactura.value = factura;
};

const filtrar = () => {};

const imprimirFactura = (id) => {
    // We can use the already open modal or fetch data to generate
    // Since we want to use the modal's HTML as template, we should set it as selected
    const factura = facturas.value.find(f => f.id === id);
    if (!factura) return;
    
    selectedFactura.value = factura;
    // Wait for DOM update
    setTimeout(() => {
        const element = document.querySelector('.invoice-body');
        const opt = {
            margin:       1,
            filename:     `factura_${id}.pdf`,
            image:        { type: 'jpeg', quality: 0.98 },
            html2canvas:  { scale: 2 },
            jsPDF:        { unit: 'in', format: 'letter', orientation: 'portrait' }
        };
        // Use html2pdf from global or import if available. 
        // Since we installed it, we should import it.
        // Dynamic import or assume global if loaded via script.
        // Let's use the import.
        import('html2pdf.js').then(html2pdf => {
            html2pdf.default().set(opt).from(element).save();
        }).catch(err => console.error(err));
    }, 500);
};
</script>

<style scoped>
.historico-container { padding: 20px; background: #f8f9fa; min-height: 100vh; font-family: 'Segoe UI', sans-serif; }
.top-bar { display: flex; justify-content: space-between; color: #666; font-size: 0.9rem; margin-bottom: 20px; }
.company-header { text-align: right; }
.card { background: white; border-radius: 8px; padding: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); }

.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
h2 { margin: 0; color: #333; font-size: 1.2rem; }

.filters { display: flex; gap: 10px; }
.form-input { padding: 5px 10px; border: 1px solid #ddd; border-radius: 4px; }
.btn-primary { background: #2196F3; color: white; border: none; padding: 6px 15px; border-radius: 4px; cursor: pointer; }
.btn-secondary { background: #6c757d; color: white; border: none; padding: 6px 15px; border-radius: 4px; cursor: pointer; }

.data-table { width: 100%; border-collapse: collapse; }
.data-table th { text-align: left; padding: 12px; border-bottom: 2px solid #eee; color: #666; font-size: 0.9rem; }
.data-table td { padding: 12px; border-bottom: 1px solid #eee; color: #333; font-size: 0.9rem; }
.empty { text-align: center; color: #999; padding: 30px; }

.badge { padding: 4px 8px; border-radius: 12px; font-size: 0.75rem; font-weight: bold; }
.badge.success { background: #e8f5e9; color: #2e7d32; }
.badge.warning { background: #fff3e0; color: #ef6c00; }

.btn-icon { background: none; border: none; cursor: pointer; padding: 4px; margin-right: 5px; }
.btn-icon:hover { background: #f0f0f0; border-radius: 4px; }
.icon-blue { stroke: #2196F3; width: 18px; height: 18px; }
.icon-dark { stroke: #666; width: 18px; height: 18px; }

/* MODAL STYLES */
.modal-overlay { position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); display: flex; justify-content: center; align-items: center; z-index: 1000; }
.invoice-modal { width: 700px; max-width: 90%; background: white; border-radius: 4px; display: flex; flex-direction: column; max-height: 90vh; }
.animate-pop { animation: popIn 0.3s ease; }
@keyframes popIn { from { transform: scale(0.9); opacity: 0; } to { transform: scale(1); opacity: 1; } }

.modal-header { padding: 15px 20px; border-bottom: 1px solid #eee; display: flex; justify-content: space-between; align-items: center; }
.close-btn { font-size: 1.5rem; cursor: pointer; color: #999; }

.invoice-body { padding: 30px 40px; overflow-y: auto; color: #333; }

/* Invoice Template Styles */
.inv-header { display: flex; justify-content: space-between; margin-bottom: 30px; }
.logo-area { text-align: center; }
.circle-logo { width: 60px; height: 60px; border: 2px solid black; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin-bottom: 5px; }
.circle-logo svg { width: 30px; height: 30px; }
.inv-title h1 { font-family: 'Helvetica', sans-serif; font-size: 2.5rem; letter-spacing: 2px; color: #111; margin: 0; }

.inv-info { margin-bottom: 30px; font-size: 1rem; line-height: 1.6; }

.inv-table { width: 100%; border-collapse: collapse; margin-bottom: 30px; }
.header-row th { background: #111; color: white; padding: 10px; text-align: left; font-weight: bold; }
.inv-table td { padding: 12px 10px; border-bottom: 1px solid #eee; }
.text-center { text-align: center; }
.text-right { text-align: right; }

.inv-totals { display: flex; flex-direction: column; align-items: flex-end; gap: 8px; }
.total-row { display: flex; gap: 40px; justify-content: flex-end; font-size: 1rem; }
.total-row.big { font-size: 1.3rem; font-weight: bold; margin-top: 10px; }

.modal-footer { padding: 15px 20px; background: #f8f9fa; border-top: 1px solid #eee; display: flex; justify-content: flex-end; gap: 10px; }
</style>
