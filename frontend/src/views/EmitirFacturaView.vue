<template>
  <div class="invoice-container">
    <NotificationToast ref="toast" />

    <!-- Header de Navegación -->
    <div class="top-bar">
        <span>Inicio > Ventas > Generar Factura</span>
        <div class="company-header">
           <span>Mantenimiento {{loggedUserName}}</span>
        <!--<small>{{ loggedUserName }}</small>-->
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
        <thead>
          <tr>
            <th>Código</th>
            <th>Cantidad</th>
            <th>Descripción</th>
            <th>Precio</th>
            <th>Descuento(%)</th>
            <th>Subtotal</th>
            <th>IVA</th>
            <th>Total</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in carrito" :key="index">
            <td>{{ item.codigo || item.id.substring(0,6) }}</td>
            <td>
                <input type="number" v-model="item.cantidad" min="1" class="qty-input" @change="recalcularItem(item)">
            </td>
            <td>{{ item.nombre }}</td>
            <td>{{ item.precio.toFixed(2) }}</td>
            <td>
                <input type="number" v-model="item.descuento" min="0" max="100" class="qty-input" @change="recalcularItem(item)">
            </td>
            <td>{{ item.subtotal.toFixed(2) }}</td>
            <td>{{ item.iva.toFixed(2) }}</td>
            <td>{{ item.total.toFixed(2) }}</td>
            <td><button @click="eliminarItem(index)" class="btn-trash">🗑️</button></td>
          </tr>
          <tr v-if="carrito.length === 0">
             <td colspan="9" class="empty-cell">0.00</td>
          </tr>
        </tbody>
      </table>
      
      <div class="total-row">
        <span>TOTAL IMPORTE</span>
        <span class="total-amount">{{ granTotal.toFixed(2) }}</span>
      </div>
    </div>

    <!-- Formas de Pago Multipago -->
    <div class="card payment-section">
        <div class="section-header">
            <span class="flex-align">
                 <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="24" height="24" class="mr-2">
                    <rect x="2" y="5" width="20" height="14" rx="2" />
                    <line x1="2" y1="10" x2="22" y2="10" />
                    <line x1="12" y1="15" x2="12" y2="15" stroke-width="3"/>
                 </svg>
                Formas de Pago
            </span>
            <div class="actions">
                <button class="btn btn-warning small" @click="agregarPago">+ Agregar Pago</button>
            </div>
        </div>
        <div class="payment-list">
            <div v-for="(pago, idx) in pagos" :key="idx" class="payment-row">
                <div class="payment-method">
                    <select v-model="pago.metodo" class="form-select full">
                        <option value="EFECTIVO">EFECTIVO</option>
                        <option value="TRANSFERENCIA">TRANSFERENCIA</option>
                    </select>
                </div>
                <div class="payment-amount">
                    <input type="number" v-model="pago.monto" class="form-input text-right" step="0.01">
                    <button class="btn-trash-red" @click="eliminarPago(idx)">⛌</button>
                </div>
            </div>
            <div class="payment-footer">
                <small>Total Pagado: $ {{ totalPagado.toFixed(2) }}</small>
                <small :class="{'text-red': faltante > 0.01, 'text-green': faltante <= 0.01}">
                    {{ faltante > 0.01 ? 'Faltante: $ ' + faltante.toFixed(2) : 'Completo' }}
                </small>
            </div>
        </div>
    </div>

    <!-- Información Adicional -->
    <div class="card info-section">
        <div class="section-header flex-align">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="20" height="20" class="mr-2">
                <circle cx="12" cy="12" r="10"></circle>
                <line x1="12" y1="8" x2="12" y2="12"></line>
                <line x1="12" y1="16" x2="12.01" y2="16"></line>
            </svg>
            Información Adicional
        </div>
        <textarea v-model="infoAdicional" class="form-input full-width" rows="2" placeholder="Ingrese información adicional..."></textarea>
    </div>

    <!-- Botones Finales -->
    <div class="footer-actions">
        <button class="btn btn-danger big" @click="$router.push('/facturar')">🚫 Cancelar</button>
        <button class="btn btn-success big" @click="guardarFactura">✅ Guardar</button>
    </div>

    <!-- MODAL CLIENTE -->
    <div v-if="mostrarModal" class="modal-overlay">
      <div class="modal-content animate-pop">
        <div class="modal-header">
            <div class="modal-title">
                <h2 class="flex-align">
                    <svg viewBox="0 0 24 24" fill="none" stroke="#28a745" stroke-width="2" width="28" height="28" class="mr-2">
                      <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
                      <circle cx="9" cy="7" r="4"></circle>
                      <path d="M23 21v-2a4 4 0 0 0-3-3.87"></path>
                      <path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
                    </svg>
                    Clientes
                </h2>
            </div>
            <span class="close-btn" @click="mostrarModal = false">✖</span>
        </div>
        
        <div class="modal-body">
            <div class="row">
                <div class="col">
                    <label>Fecha de emisión *</label>
                    <input type="date" :value="today" disabled class="form-input">
                </div>
                <!-- Autocomplete Client Section -->
                <div class="col search-col relative">
                    <label>Identificación *</label>
                    <div class="input-with-btn">
                        <input 
                            v-model="tempCliente.identificacion" 
                            class="form-input" 
                            @input="onSearchInput"
                            placeholder="Buscar..."
                            autocomplete="off"
                        >
                        <button class="btn-search detail-icon-btn">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                                <polyline points="14 2 14 8 20 8"></polyline>
                                <line x1="16" y1="13" x2="8" y2="13"></line>
                                <line x1="16" y1="17" x2="8" y2="17"></line>
                                <polyline points="10 9 9 9 8 9"></polyline>
                            </svg>
                        </button>
                    </div>
                    <!-- Client Autocomplete Dropdown -->
                    <ul v-if="sugerencias.length" class="autocomplete-list">
                        <li v-for="sug in sugerencias" :key="sug.id" @click="seleccionarSugerencia(sug)">
                            <strong>{{ sug.identificacion }}</strong> - {{ sug.nombre }}
                        </li>
                    </ul>
                </div>
                <div class="col">
                    <label>Tipo Identificación *</label>
                    <select v-model="tempCliente.tipoId" class="form-input">
                        <option value="CEDULA">Cédula</option>
                        <option value="RUC">RUC</option>
                    </select>
                </div>
                 <div class="col">
                    <label>Teléfono</label>
                    <input v-model="tempCliente.telefono" class="form-input">
                </div>
            </div>

            <div class="row">
                <div class="col full">
                    <label>Nombres *</label>
                    <input v-model="tempCliente.nombre" class="form-input">
                </div>
                <div class="col full">
                    <label>Correo *</label>
                    <input v-model="tempCliente.email" class="form-input">
                </div>
            </div>

            <div class="row">
                <div class="col full">
                    <label>Dirección *</label>
                    <input v-model="tempCliente.direccion" class="form-input">
                </div>
            </div>
            
            <div class="info-box">
                NOTA: A medida que escribas se mostrarán las coincidencias de clientes ya registrados.
            </div>
        </div>
        
        <div class="modal-footer">
            <button class="btn btn-secondary" @click="mostrarModal = false">Cancelar</button>
            <button class="btn btn-primary" @click="confirmarCliente">Guardar</button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import NotificationToast from '../components/NotificationToast.vue';

const router = useRouter();
const toast = ref(null);
const today = new Date().toISOString().split('T')[0];
const mostrarModal = ref(true); 
const cliente = ref(null);
const busquedaProducto = ref('');
const carrito = ref([]);
const sugerencias = ref([]);
const prodSugerencias = ref([]); 
const loggedUserName = ref('USUARIO');
const infoAdicional = ref('');

// Payment Methods
const pagos = ref([
    { metodo: 'EFECTIVO', monto: 0 }
]);

const tempCliente = ref({
    tipoId: 'CEDULA',
    identificacion: '',
    nombre: '',
    email: '',
    telefono: '',
    direccion: ''
});

onMounted(() => {
    const userStr = localStorage.getItem('user');
    if (userStr) {
        const u = JSON.parse(userStr);
        loggedUserName.value = u.nombre || u.username || 'USUARIO';
    }
});

// Watch total to auto-update single payment default
const granTotal = computed(() => carrito.value.reduce((acc, item) => acc + item.total, 0));

watch(granTotal, (newTotal) => {
    if (pagos.value.length === 1) pagos.value[0].monto = newTotal;
});

const totalPagado = computed(() => pagos.value.reduce((acc, p) => acc + (parseFloat(p.monto) || 0), 0));
const faltante = computed(() => Math.max(0, granTotal.value - totalPagado.value));

// --- Client Autocomplete ---
let searchTimeout;
const onSearchInput = () => {
    clearTimeout(searchTimeout);
    if (!tempCliente.value.identificacion) {
        sugerencias.value = [];
        return;
    }
    
    searchTimeout = setTimeout(async () => {
        try {
            const res = await fetch(`http://localhost:8080/api/clientes/autocomplete?query=${tempCliente.value.identificacion}`);
            if (res.ok) {
                const data = await res.json();
                sugerencias.value = data;
                
                if (data.length === 0) {
                     sugerencias.value.push({
                        id: 'new',
                        identificacion: tempCliente.value.identificacion,
                        nombre: 'Cliente no encontrado - CREAR NUEVO',
                        isNew: true
                     });
                }
            }
        } catch (e) {
            console.error("Error searching clients");
        }
    }, 300);
};

const seleccionarSugerencia = (sug) => {
    if (sug.isNew) {
        mostrarModal.value = true;
        tempCliente.value.identificacion = sug.identificacion;
        tempCliente.value.nombre = ''; tempCliente.value.email = '';
        tempCliente.value.telefono = ''; tempCliente.value.direccion = '';
    } else {
        cliente.value = sug;
        mostrarModal.value = false;
        tempCliente.value = { ...sug, tipoId: 'CEDULA' }; 
    }
    sugerencias.value = [];
};

// --- Product Autocomplete ---
let prodTimeout;
const onSearchProduct = () => {
    clearTimeout(prodTimeout);
    if (!busquedaProducto.value) {
        prodSugerencias.value = [];
        return;
    }
    prodTimeout = setTimeout(async () => {
        try {
            const res = await fetch(`http://localhost:8080/api/productos/buscar?query=${busquedaProducto.value}`);
            if(res.ok) {
                prodSugerencias.value = await res.json();
            }
        } catch(e) {}
    }, 300);
};

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
    if (item.cantidad <= 0) {
        const idx = carrito.value.indexOf(item);
        if (idx !== -1) {
            eliminarItem(idx);
            toast.value.add('Producto eliminado por cantidad 0', 'info');
        }
        return;
    }
    
    if (item.cantidad > item.stock) {
        toast.value.add(`Stock insuficiente. Solo hay ${item.stock} unidades disponibles.`, 'error');
        item.cantidad = item.stock;
    }
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
.btn-trash { border: none; background: none; cursor: pointer; color: #e74c3c; padding: 5px; display: flex; align-items: center; justify-content: center; transition: background 0.2s; border-radius: 4px; }
.btn-trash:hover { background: #ffebee; }
.btn-trash svg { width: 18px; height: 18px; }

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
.btn-trash-red { background: #ff5252; color: white; border: none; padding: 0 10px; cursor: pointer; display: flex; align-items: center; justify-content: center; transition: background 0.2s; }
.btn-trash-red:hover { background: #ff1744; }
.btn-trash-red svg { width: 16px; height: 16px; }

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
.btn-search { background: white; border: 1px solid #ddd; border-left: none; padding: 0 10px; border-top-right-radius: 4px; border-bottom-right-radius: 4px; cursor: pointer; transition: all 0.2s; }
.detail-icon-btn svg { width: 20px; height: 20px; stroke: #666; }
.detail-icon-btn:hover { background: #f0f0f0; }
.detail-icon-btn:hover svg { stroke: #2196F3; }
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
