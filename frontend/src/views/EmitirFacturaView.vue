<template>
  <div class="invoice-container">
    <NotificationToast ref="toast" />

    <!-- Header de Navegación -->
    <div class="top-bar">
        <span>Inicio > Ventas > Generar Factura</span>
        <div class="company-header">
            <span>Mantenimiento</span>
            <small>{{ loggedUserName }}</small>
        </div>
    </div>

    <!-- Sección Cliente -->
    <div class="card client-section">
      <div class="client-row">
        <div class="client-info">
            <span class="icon-svg">
                <!-- User Icon (Retencion/Professional) -->
                <svg viewBox="0 0 24 24" fill="none" class="svg-icon-blue">
                   <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" stroke="#2c3e50" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                   <circle cx="12" cy="7" r="4" stroke="#2c3e50" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            </span>
            <strong>Cliente:</strong> 
            <span>{{ cliente?.nombre || 'CONSUMIDOR FINAL' }}</span>
            <strong class="ml-3">Identificación:</strong>
            <span>{{ cliente?.identificacion || '9999999999999' }}</span>
        </div>
        <div class="client-actions">
            <button class="btn btn-info" @click="mostrarModal = true">
                <svg viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" width="16" height="16"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path></svg>
                Editar Cliente
            </button>
            <select class="form-select user-select">
                <option>01001 - Cajero Principal</option>
            </select>
        </div>
      </div>
    </div>

    <!-- Sección Detalle (Grid) -->
    <div class="card detail-section">
      <div class="toolbar">
        <div class="left-tools">
            <svg viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2" width="20" height="20" class="mr-2">
                <circle cx="11" cy="11" r="8"></circle><line x1="21" y1="21" x2="16.65" y2="16.65"></line>
            </svg>
            <strong>Detalle {{ carrito.length }}</strong>
        </div>
        <div class="right-tools">
            <div class="search-group relative">
                <span class="barcode-icon">||||</span>
                <input 
                    v-model="busquedaProducto" 
                    @input="onSearchProduct"
                    placeholder="Buscar producto por nombre..." 
                    class="form-input search-input"
                >
                <button class="btn-icon">
                    <svg viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2" width="18" height="18"><circle cx="11" cy="11" r="8"></circle><line x1="21" y1="21" x2="16.65" y2="16.65"></line></svg>
                </button>
                
                <ul v-if="prodSugerencias.length" class="autocomplete-list product-list">
                    <li v-for="prod in prodSugerencias" :key="prod.id" @click="seleccionarProducto(prod)" :class="{'disabled-item': prod.stock <= 0}">
                        <strong>{{ prod.nombre }}</strong> - $ {{ prod.precio }}
                        <span v-if="prod.stock <= 0" class="stock-alert">(SIN STOCK)</span>
                        <span v-else class="stock-info">(Stock: {{ prod.stock }})</span>
                    </li>
                </ul>
            </div>
            
            <button class="btn btn-danger" @click="limpiarCarrito">
                <svg viewBox="0 0 24 24" fill="none" class="svg-icon-white" stroke-width="2"><polyline points="3 6 5 6 21 6"></polyline><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path><line x1="10" y1="11" x2="10" y2="17"></line><line x1="14" y1="11" x2="14" y2="17"></line></svg>
                Limpiar
            </button>
        </div>
      </div>

      <table class="invoice-table">
        <!-- ... -->
      </table>
      <!-- ... -->
    </div>

    <!-- ... -->
</template>

<script setup>
// ... existing imports ...

// ... existing code ...

const seleccionarProducto = (prod) => {
    if (prod.stock <= 0) {
        toast.value.add('No hay existencias de este producto', 'error');
        return;
    }
    agregarAlCarrito(prod);
    busquedaProducto.value = '';
    prodSugerencias.value = [];
};

const agregarAlCarrito = (prod) => {
    // Check if adding more would exceed stock
    const exists = carrito.value.find(c => c.id === prod.id);
    if(exists) {
        if (exists.cantidad + 1 > prod.stock) {
            toast.value.add(`No hay suficiente stock. Disponible: ${prod.stock}`, 'warning');
            return;
        }
        exists.cantidad++;
        recalcularItem(exists);
        toast.value.add(`Cantidad actualizada: ${prod.nombre}`, 'info');
        return;
    }

    const item = {
        ...prod,
        cantidad: 1,
        descuento: 0,
        subtotal: prod.precio,
        iva: prod.precio * 0.12, 
        total: prod.precio * 1.12
    };
    carrito.value.push(item);
    // Auto adjust payment if single
    if (pagos.value.length === 1) pagos.value[0].monto = granTotal.value;
};

const recalcularItem = (item) => {
    const precioDesc = item.precio * (1 - (item.descuento || 0)/100);
    item.subtotal = precioDesc * item.cantidad;
    item.iva = item.subtotal * 0.12;
    item.total = item.subtotal + item.iva;
};

const eliminarItem = (index) => {
    carrito.value.splice(index, 1);
};
const limpiarCarrito = () => { carrito.value = []; rangePayments(); };

// Payment Logic
const agregarPago = () => {
    pagos.value.push({ metodo: 'EFECTIVO', monto: faltante.value });
};
const eliminarPago = (idx) => {
    if (pagos.value.length > 1) pagos.value.splice(idx, 1);
};
const rangePayments = () => {
    if(pagos.value.length === 1) pagos.value[0].monto = granTotal.value;
}

const confirmarCliente = async () => {
    try {
        const res = await fetch('http://localhost:8080/api/clientes', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(tempCliente.value)
        });
        if(res.ok) {
            cliente.value = await res.json();
            mostrarModal.value = false;
            toast.value.add('Cliente guardado exitosamente', 'success');
        } else {
             toast.value.add('Error al guardar cliente', 'error');
        }
    } catch(e) {
        toast.value.add('Error de conexión', 'error');
    }
};

const guardarFactura = async () => {
    if(!cliente.value) {
        toast.value.add("Debe seleccionar un cliente", 'error');
        return;
    }
    if(carrito.value.length === 0) {
        toast.value.add("Debe agregar productos", 'error');
        return;
    }
    if(Math.abs(totalPagado.value - granTotal.value) > 0.05) {
        toast.value.add(`El pago no cubre el total de la factura. Faltante: ${faltante.value.toFixed(2)}`, 'error');
        return;
    }

    const invoicePayload = {
        clienteId: cliente.value.id,
        productos: carrito.value.map(p => ({
            productoId: p.id,
            cantidad: p.cantidad
        })),
        pagos: pagos.value
    };

    try {
        const res = await fetch('http://localhost:8080/api/facturas', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(invoicePayload)
        });
        
        if (res.ok) {
            toast.value.add("Factura Guardada Exitosamente", 'success');
            setTimeout(() => {
                router.push('/facturar');
            }, 1500);
        } else {
            const err = await res.text();
            toast.value.add("Error al guardar: " + err, 'error');
        }
    } catch (error) {
        toast.value.add("Error de conexión al servidor", 'error');
    }
};
</script>

<style scoped>
.invoice-container { padding: 10px; background: #f4f6f9; min-height: 100vh; font-family: 'Segoe UI', sans-serif; }
.top-bar { display: flex; justify-content: space-between; color: #666; font-size: 0.9rem; margin-bottom: 10px; }
.company-header { text-align: right; }
.card { background: white; border-radius: 4px; padding: 10px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); margin-bottom: 15px; }

/* Client Section */
.client-row { display: flex; justify-content: space-between; align-items: center; }
.client-info { display: flex; align-items: center; }
.icon-svg { margin-right: 10px; display: flex; align-items: center; }
.ml-3 { margin-left: 15px; }
.client-actions { display: flex; gap: 10px; }

/* Toolbar */
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; flex-wrap: wrap; gap: 10px; }
.left-tools { display: flex; align-items: center; font-size: 1rem; color: #555; }
.right-tools { display: flex; gap: 10px; align-items: center; }
.form-select, .form-input { padding: 6px 10px; border: 1px solid #ddd; border-radius: 4px; font-size: 0.9rem; }
.small-input { width: 100px; }
.search-group { display: flex; align-items: center; border: 1px solid #ddd; border-radius: 4px; background: white; width: 300px; }
.search-input { border: none; outline: none; flex: 1; padding: 8px; }
.btn-icon { background: none; border: none; cursor: pointer; color: #666; padding: 0 8px; display: flex; align-items: center; }
.barcode-icon { padding: 0 8px; font-weight: bold; color: #999; letter-spacing: 2px; }

/* Table */
.invoice-table { width: 100%; border-collapse: collapse; margin-top: 10px; }
.invoice-table th { background: #f8f9fa; padding: 8px; text-align: left; font-size: 0.85rem; color: #666; border-bottom: 2px solid #ddd; }
.invoice-table td { padding: 8px; border-bottom: 1px solid #eee; font-size: 0.9rem; }
.qty-input { width: 60px; padding: 4px; border: 1px solid #ddd; border-radius: 3px; }
.btn-trash { border: none; background: none; cursor: pointer; color: #e74c3c; font-size: 1.2rem; }
.empty-cell { text-align: right; color: #999; padding: 20px; }
.total-row { display: flex; justify-content: flex-end; align-items: center; padding: 15px; background: #f1f2f6; gap: 20px; font-weight: bold; }
.total-amount { font-size: 1.2rem; }

/* Payments */
.section-header { font-weight: bold; margin-bottom: 10px; display: flex; justify-content: space-between; }
.payment-row { display: flex; gap: 0; border: 1px solid #eee; margin-bottom: 5px; }
.payment-method { flex: 2; border-right: 1px solid #eee; }
.payment-amount { flex: 1; display: flex; }
.payment-footer { text-align: right; margin-top: 10px; display: flex; flex-direction: column; }
.text-red { color: red; font-weight: bold; }
.text-green { color: green; font-weight: bold; }

.full { width: 100%; border: none; }
.text-right { text-align: right; }
.btn-trash-red { background: #ff5252; color: white; border: none; padding: 0 15px; cursor: pointer; }

/* Footer */
.footer-actions { display: flex; justify-content: flex-end; gap: 15px; margin-top: 20px; }
.big { padding: 10px 30px; font-size: 1rem; }

/* Buttons */
.btn { padding: 6px 15px; border: none; border-radius: 4px; cursor: pointer; color: white; font-weight: 500; display: flex; align-items: center; gap: 5px; }
.btn-info { background: #17a2b8; }
.btn-warning { background: #ffc107; color: #333; }
.btn-danger { background: #dc3545; }
.btn-success { background: #28a745; }
.btn-secondary { background: #6c757d; }

/* Modal & Autocomplete */
.relative { position: relative; }
.autocomplete-list {
    position: absolute; top: 100%; left: 0; width: 100%;
    background: white; border: 1px solid #ddd; border-radius: 4px;
    z-index: 1001; list-style: none; padding: 0; margin: 0;
    max-height: 150px; overflow-y: auto; box-shadow: 0 4px 6px rgba(0,0,0,0.1);
}
.autocomplete-list li { padding: 10px; cursor: pointer; border-bottom: 1px solid #eee; }
.autocomplete-list li:hover { background: #f0f0f0; }

.modal-overlay { position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.6); display: flex; justify-content: center; align-items: center; z-index: 999; }
.modal-content { background: white; width: 600px; border-radius: 8px; overflow: hidden; box-shadow: 0 5px 20px rgba(0,0,0,0.2); }
.modal-header { background: white; padding: 15px 20px; border-bottom: 1px solid #eee; display: flex; justify-content: space-between; align-items: center; }
.modal-title { display: flex; align-items: center; gap: 10px; }
.close-btn { cursor: pointer; font-size: 1.2rem; color: #999; }
.modal-body { padding: 20px; background: #fafafa; }
.row { display: flex; gap: 15px; margin-bottom: 15px; }
.col { flex: 1; display: flex; flex-direction: column; }
.full { flex: 100%; }
.search-col { flex: 1.5; }
label { font-size: 0.85rem; color: #666; margin-bottom: 5px; }
.input-with-btn { display: flex; }
.input-with-btn input { flex: 1; border-top-right-radius: 0; border-bottom-right-radius: 0; }
.btn-search { background: white; border: 1px solid #ddd; border-left: none; padding: 0 10px; border-top-right-radius: 4px; border-bottom-right-radius: 4px; cursor: pointer; }
.info-box { background: #e3f2fd; color: #0277bd; padding: 10px; font-size: 0.8rem; border-left: 4px solid #0277bd; margin-top: 10px; }
.modal-footer { padding: 15px 20px; background: white; border-top: 1px solid #eee; display: flex; justify-content: flex-end; gap: 10px; }
.flex-align { display: flex; align-items: center; }
.mr-2 { margin-right: 8px; }
.svg-icon-blue { width: 32px; height: 32px; stroke: #2196F3; }
.svg-icon-white { width: 16px; height: 16px; stroke: white; }

.stock-alert { color: #f44336; font-weight: bold; margin-left: 5px; font-size: 0.8rem; }
.stock-info { color: #2ecc71; font-weight: bold; margin-left: 5px; font-size: 0.8rem; }
.disabled-item { opacity: 0.6; cursor: not-allowed; background: #f9f9f9; }
</style>
