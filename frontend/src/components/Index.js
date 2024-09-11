import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

function Index() {
    const [products, setProducts] = useState([]);

    useEffect(() => {
        fetchProducts();
    }, []);

    const fetchProducts = async () => {
        const response = await fetch('/api/products');
        const data = await response.json();
        setProducts(data);
    };

    return (
        <div className="index-page">
            {/* Header with Logo and Login */}
            <header className="header">
                <div className="logo">
                    <Link to="/">
                        <img src="/static/images/logo.jpg" alt="Logo" />
                        <span>ONLINE BUY-SELL PLATFORM EHA</span>
                    </Link>
                </div>
                <div className="login-btn">
                    <button>Sign in</button>
                </div>
            </header>

            {/* Search Section */}
            <div className="search-section">
                <h2>Search Product</h2>
                <div className="search-fields">
                    <select>
                        <option value="all">All Cities</option>
                        <option value="city1">City 1</option>
                        <option value="city2">City 2</option>
                    </select>

                    <input type="text" placeholder="Search by ads" className="search-input" />
                </div>
                <div className="search-btn-container">
                    <button className="search-btn">Search</button>
                </div>
            </div>

            {/* Products Section */}
            <div className="product-list">
                {products.length > 0 ? (
                    products.map((product) => (
                        <div key={product.id} className="product-item">
                            <h3>{product.name}</h3>
                            <p>{product.description}</p>
                            <button className="buy-btn">Buy Now</button>
                        </div>
                    ))
                ) : (
                    <p>No products found</p>
                )}
            </div>
        </div>
    );
}

export default Index;
