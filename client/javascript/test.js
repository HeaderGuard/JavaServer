document.getElementById("create").addEventListener("click", create);
document.getElementById("read").addEventListener("click", read);
document.getElementById("update").addEventListener("click", update);
document.getElementById("delete").addEventListener("click", del);

function create()
{
    const req = JSON.stringify(
        {
            name: document.getElementById("name").value,
            age: document.getElementById("age").value
        });
    request(req, "/users/add");
}
function read()
{
    const req = JSON.stringify(
        {
            name: document.getElementById("name").value
        });
    Object.freeze(req);
    request(req, "/users/find");
}
function update()
{
    const req = JSON.stringify(
        {
            name: document.getElementById("name").value,
            age: document.getElementById("age").value
        });
    Object.freeze(req);
    request(req, "/users/update");
}
function del()
{
    const req = JSON.stringify(
        {
            name: document.getElementById("name").value
        });
    Object.freeze(req);
    request(req, "/users/delete");
}

function request(body, endpoint)
{
    Object.freeze(body);
    fetch(endpoint,
        {
            method: "POST",
            headers:
                {
                    "Content-Type": "application/json"
                },
            body: body
        })
        .then(res => res.json())
        .then(data => console.log(data));
}