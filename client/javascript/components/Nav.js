class Nav extends HTMLElement
{
    constructor()
    {
        super();
        this.render();
    }
    render()
    {
        const pageList = [{title: "Home", path: "/"}, {title: "Ez", path: "/ez"}];
        const pages = pageList.map(page =>
        {
            return `<li><a href="${page.path}" class="bar-link">${page.title}</a></li>`;
        });
        this.innerHTML =
            `
            <nav>
                <ul class="bar-links">${pages}</ul>
            </nav>
            `;
    }
}

customElements.define("navbar-component", Nav);