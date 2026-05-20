const CART_KEY = "cybervoid_cart";

const formatCurrency = (value) =>
  new Intl.NumberFormat("es-ES", {
    style: "currency",
    currency: "EUR",
  }).format(value);

const getCart = () => JSON.parse(localStorage.getItem(CART_KEY) || "[]");

const saveCart = (cart) => {
  localStorage.setItem(CART_KEY, JSON.stringify(cart));
  updateCartBadge();
};

const productFromButton = (button) => ({
  id: button.dataset.id,
  name: button.dataset.name,
  size: button.dataset.size || "M",
  color: button.dataset.color || "Negro",
  price: Number(button.dataset.price || 0),
  image: button.dataset.image || "",
  quantity: 1,
});

const sameProduct = (item, product) =>
  item.id === product.id && item.size === product.size && item.color === product.color;

const addToCart = (product) => {
  const cart = getCart();
  const existing = cart.find((item) => sameProduct(item, product));

  if (existing) {
    existing.quantity += 1;
  } else {
    cart.push(product);
  }

  saveCart(cart);
  showToast(`${product.name} añadido al carrito`);
};

const removeFromCart = (index) => {
  const cart = getCart();
  cart.splice(index, 1);
  saveCart(cart);
  renderCartPage();
};

const updateQuantity = (index, quantity) => {
  const cart = getCart();
  const nextQuantity = Math.max(1, Number(quantity || 1));
  cart[index].quantity = nextQuantity;
  saveCart(cart);
  renderCartPage();
};

const updateCartBadge = () => {
  const badge = document.querySelector("[data-cart-count]");
  if (!badge) return;

  const count = getCart().reduce((total, item) => total + item.quantity, 0);
  badge.textContent = count;
  badge.classList.toggle("d-none", count === 0);
};

const showToast = (message) => {
  const container = document.querySelector("[data-toast-container]");
  if (!container || !window.bootstrap) return;

  const toastElement = document.createElement("div");
  toastElement.className = "toast align-items-center text-bg-dark border-0";
  toastElement.setAttribute("role", "status");
  toastElement.setAttribute("aria-live", "polite");
  toastElement.setAttribute("aria-atomic", "true");
  toastElement.innerHTML = `
    <div class="d-flex">
      <div class="toast-body">${message}</div>
      <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Cerrar"></button>
    </div>
  `;

  container.appendChild(toastElement);
  const toast = new bootstrap.Toast(toastElement, { delay: 2200 });
  toast.show();
  toastElement.addEventListener("hidden.bs.toast", () => toastElement.remove());
};

const renderCartPage = () => {
  const cartBody = document.querySelector("[data-cart-body]");
  const cartTotal = document.querySelector("[data-cart-total]");
  const emptyState = document.querySelector("[data-cart-empty]");
  const cartTable = document.querySelector("[data-cart-table]");

  if (!cartBody || !cartTotal) return;

  const cart = getCart();
  const total = cart.reduce((sum, item) => sum + item.price * item.quantity, 0);

  cartBody.innerHTML = cart
    .map(
      (item, index) => `
        <tr>
          <td>
            <div class="fw-bold text-uppercase">${item.name}</div>
            <small class="text-muted">${item.id}</small>
          </td>
          <td>${item.size}</td>
          <td>${item.color}</td>
          <td style="width: 120px;">
            <input class="form-control form-control-sm" type="number" min="1" value="${item.quantity}" data-cart-quantity="${index}" aria-label="Cantidad de ${item.name}">
          </td>
          <td>${formatCurrency(item.price)}</td>
          <td class="fw-bold">${formatCurrency(item.price * item.quantity)}</td>
          <td>
            <button class="btn btn-sm btn-outline-dark" data-cart-remove="${index}">Eliminar</button>
          </td>
        </tr>
      `
    )
    .join("");

  cartTotal.textContent = formatCurrency(total);
  emptyState?.classList.toggle("d-none", cart.length > 0);
  cartTable?.classList.toggle("d-none", cart.length === 0);
};

document.addEventListener("click", (event) => {
  const addButton = event.target.closest("[data-add-to-cart]");
  if (addButton) {
    addToCart(productFromButton(addButton));
    return;
  }

  const removeButton = event.target.closest("[data-cart-remove]");
  if (removeButton) {
    removeFromCart(Number(removeButton.dataset.cartRemove));
  }
});

document.addEventListener("input", (event) => {
  const quantityInput = event.target.closest("[data-cart-quantity]");
  if (quantityInput) {
    updateQuantity(Number(quantityInput.dataset.cartQuantity), quantityInput.value);
  }
});

document.addEventListener("DOMContentLoaded", () => {
  updateCartBadge();
  renderCartPage();
});
