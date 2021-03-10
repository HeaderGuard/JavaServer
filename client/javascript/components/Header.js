class Header extends HTMLElement
{
    constructor()
    {
        super();
        this.render();
    }
    render()
    {
        this.innerHTML =
            `<header class="header">
                <h1 class="site-title">Hello World</h1>
                <h2 class="page-title">${this.getAttribute("pageTitle")}</h2>
            </header>`;
    }
}
window.customElements.define("header-component", Header);