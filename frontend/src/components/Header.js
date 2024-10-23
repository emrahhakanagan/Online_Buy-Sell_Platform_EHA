import React from 'react';
import LanguageSelector from './LanguageSelector';
import './Header.css';

const Header = () => {
    return (
        <header className="app-header">
            <div className="header-content">
                <div className="logo">
                    <img src="/assets/logo.png" alt="EHA Logo" /> {/* Убедись, что путь к логотипу корректный */}
                </div>
                <div className="title">
                    <h1>ONLINE BUY-SELL PLATFORM <span>eha</span></h1>
                </div>
                <div className="login-button">
                    <button className="sign-in-button">Sign In</button>
                </div>
                <LanguageSelector />
            </div>
        </header>
    );
};

export default Header;
