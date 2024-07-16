document.addEventListener('DOMContentLoaded', loadHome);

const templateBuilder = {
    build: (template, data, target) => {
        // Implementation for building template
    },
    clear: (target) => {
        // Implementation for clearing template
    }
};

const userService = {
    login: (username, password) => {
        // Implementation for user login
    }
};

const productService = {
    search: () => {
        // Implementation for product search
    },
    addCategoryFilter: (category) => {
        // Implementation for adding category filter
    },
    addColorFilter: (color) => {
        // Implementation for adding color filter
    },
    addMinPriceFilter: (price) => {
        // Implementation for adding minimum price filter
    },
    addMaxPriceFilter: (price) => {
        // Implementation for adding maximum price filter
    }
};

const categoryService = {
    getAllCategories: (callback) => {
        // Implementation for getting all categories
        callback();
    }
};

const profileService = {
    loadProfile: () => {
        // Implementation for loading profile
    },
    updateProfile: (profile) => {
        // Implementation for updating profile
    }
};

const cartService = {
    loadCartPage: () => {
        // Implementation for loading cart page
    },
    clearCart: () => {
        // Implementation for clearing cart
    }
};

function showLoginForm() {
    templateBuilder.build('login-form', {}, 'login');
}

function hideModalForm() {
    templateBuilder.clear('login');
}

function login() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    userService.login(username, password);
    hideModalForm();
}

function showImageDetailForm(product, imageUrl) {
    const imageDetail = {
        name: product,
        imageUrl: imageUrl
    };

    templateBuilder.build('image-detail', imageDetail, 'login');
}

function loadHome() {
    templateBuilder.build('home', {}, 'main');
    productService.search();
    categoryService.getAllCategories(loadCategories);
}

function editProfile() {
    profileService.loadProfile();
}

function saveProfile() {
    const profile = {
        firstName: document.getElementById("firstName").value,
        lastName: document.getElementById("lastName").value,
        phone: document.getElementById("phone").value,
        email: document.getElementById("email").value,
        address: document.getElementById("address").value,
        city: document.getElementById("city").value,
        state: document.getElementById("state").value,
        zip: document.getElementById("zip").value
    };

    profileService.updateProfile(profile);
}

function showCart() {
    cartService.loadCartPage();
}

function clearCart() {
    cartService.clearCart();
    cartService.loadCartPage();
}

function setCategory(control) {
    productService.addCategoryFilter(control.value);
    productService.search();
}

function setColor(control) {
    productService.addColorFilter(control.value);
    productService.search();
}

function setMinPrice(control) {
    document.getElementById("min-price-display").innerText = control.value;
    productService.addMinPriceFilter(control.value || "");
    productService.search();
}

function setMaxPrice(control) {
    document.getElementById("max-price-display").innerText = control.value;
    productService.addMaxPriceFilter(control.value != 1500 ? control.value : "");
    productService.search();
}

function closeError(control) {
    setTimeout(() => control.click(), 3000);
}
