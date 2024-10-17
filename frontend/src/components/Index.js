import React, { useState } from 'react';
import SignInModal from './SignInModal'; // Импортируем компонент модального окна

function Index() {
    const [isModalOpen, setIsModalOpen] = useState(false); // Хук состояния для управления окном

    const openModal = () => setIsModalOpen(true); // Открытие модального окна
    const closeModal = () => setIsModalOpen(false); // Закрытие модального окна

    return (
        <div className="container">
            <header className="header">
                <div className="logo-container">
                    <img src={require('../logo.jpg')} alt="EHA Logo" className="logo" />
                </div>
                <div className="banner-container">
                    <h1>ONLINE BUY-SELL PLATFORM eha</h1>
                    {/* Связываем кнопку Sign In с открытием модального окна */}
                    <button className="login-btn" onClick={openModal}>Sign In</button>
                </div>
            </header>
            <section className="search-section">
                <h2>Search Product</h2>
                <form>
                    <div className="search-options">
                        <label>
                            <select>
                                <option value="all-cities">All Cities</option>
                                <option value="city1">City 1</option>
                                <option value="city2">City 2</option>
                            </select>
                        </label>
                        <input type="text" placeholder="Search by ads" />
                    </div>
                    <div className="search-btn-container">
                        <button className="search-btn">Search</button>
                    </div>
                </form>
            </section>
            <section className="products">
                <p>No products found on our platform.</p>
            </section>

            {/* Вставляем модальное окно */}
            <SignInModal isOpen={isModalOpen} onClose={closeModal} />
        </div>
    );
}

export default Index;
