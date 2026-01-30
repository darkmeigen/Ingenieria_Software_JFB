
<template>
  <div class="container">
    <div class="header-section">
      <h1>Nueva Factura</h1>
      <p>Emisión de comprobantes electrónicos</p>
    </div>

    <div class="erp-grid">
      <div class="card form-panel">
        <h3>1. Datos del Cliente</h3>
        <select v-model="clienteId">
          <option value="1">Juan Baez (RUC: 1700...001)</option>
          <option value="2">Maria Lopez (RUC: 1700...002)</option>
        </select>

        <hr class="divider">

        <h3>2. Agregar Productos</h3>
        <label>Seleccionar Producto</label>
        <select v-model="productoSeleccionado">
          <option :value="{id: 1, nombre: 'Laptop Dell', precio: 1200.00}">Laptop Dell ($1200.00)</option>
          <option :value="{id: 2, nombre: 'Mouse Logitech', precio: 25.00}">Mouse Logitech ($25.00)</option>
          <option :value="{id: 3, nombre: 'Teclado Mecanico', precio: 80.00}">Teclado Mecánico ($80.00)</option>
        </select>

        <div class="row">
          <div class="col">
            <label>Cantidad</label>
            <input type="number" v-model="cantidad" min="1" value="1">
          </div>
          <div class="col" style="display:flex; align-items:center;">
             <button @click="agregarItem" class="btn btn-primary full-width">+ Agregar</button>
          </div>
        </div>
      </div>

      <div class="card summary-panel">
        <h3>Detalle de Factura</h3>
        
        <div v-if="carrito.length === 0" class="empty-state">
          No hay productos agregados
        </div>

        <table v-else>
          <thead>
            <tr>
              <th>Producto</th>
              <th>Cant.</th>
              <th>Subtotal</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in carrito" :key="index">
              <td>{{ item.nombre }}</td>
              <td>{{ item.cantidad }}</td>
              <td>${{ item.subtotal.toFixed(2) }}</td>
              <td>
                <button @click="eliminarItem(index)" class="btn-danger small">x</button>
              </td>
            </tr>
          </tbody>
        </table>

        <div class="totals-section" v-if="carrito.length > 0">
          <div class="total-row">
            <span>Subtotal:</span>
            <span>${{ totalCalculado.toFixed(2) }}</span>
          </div>
          <div class="total-row">
            <span>IVA (15%):</span>
            <span>${{ (totalCalculado * 0.15).toFixed(2) }}</span>
          </div>
          <div class="total-row grand-total">
            <span>TOTAL:</span>
            <span>${{ (totalCalculado * 1.15).toFixed(2) }}</span>
          </div>

          <button @click="enviarFactura" class="btn btn-success full-width mt-20"> Emitir Factura</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';

const clienteId = ref(1);
const productoSeleccionado = ref(null);
const cantidad = ref(1);
const carrito = ref([]);

const totalCalculado = computed(() => {
  return carrito.value.reduce((acc, item) => acc + item.subtotal, 0);
});

const agregarItem = () => {
  if (productoSeleccionado.value && cantidad.value > 0) {
    carrito.value.push({
      productoId: productoSeleccionado.value.id,
      nombre: productoSeleccionado.value.nombre,
      cantidad: cantidad.value,
      subtotal: productoSeleccionado.value.precio * cantidad.value
    });
    cantidad.value = 1;
  }
};

const eliminarItem = (index) => {
  carrito.value.splice(index, 1);
};

const enviarFactura = async () => {
  const facturaDTO = {
    clienteId: parseInt(clienteId.value),
    productos: carrito.value.map(item => ({
      productoId: item.productoId,
      cantidad: item.cantidad
    }))
  };

  try {
    const response = await fetch('http://localhost:8080/api/facturas', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(facturaDTO)
    });

    if (response.ok) {
      const data = await response.json();
      alert(`Factura N° ${data.idFactura} creada exitosamente!\nTotal: $${data.total}`);
      carrito.value = [];
    } else {
      alert(" Error al procesar. Revisa el Stock en el servidor.");
    }
  } catch (error) {
    console.error(error);
    alert("Error de conexión. ¿Está encendido el Backend?");
  }
};
</script>

<style scoped>
.header-section { margin-bottom: 30px; }
.erp-grid { display: grid; grid-template-columns: 1fr 1.5fr; gap: 20px; }
.divider { margin: 20px 0; border: 0; border-top: 1px solid #eee; }
.row { display: flex; gap: 10px; }
.col { flex: 1; }
.full-width { width: 100%; }
.mt-20 { margin-top: 20px; }
.small { padding: 5px 10px; font-size: 0.8rem; }

.totals-section { background: #f8f9fa; padding: 20px; margin-top: 20px; border-radius: 8px; }
.total-row { display: flex; justify-content: space-between; margin-bottom: 10px; font-size: 1.1rem; }
.grand-total { font-weight: bold; font-size: 1.4rem; color: var(--primary); border-top: 2px solid #ddd; padding-top: 10px; }
.empty-state { text-align: center; padding: 40px; color: #999; border: 2px dashed #ddd; border-radius: 8px; }
</style>