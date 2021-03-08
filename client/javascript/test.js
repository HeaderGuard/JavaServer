create();
read();
update();
del();
read();

function create()
{
    const req = JSON.stringify(
        {
            name: "Jimmy",
            age: 18
        });
    Object.freeze(req);
    fetch("/users/add",
        {
            method: "POST",
            headers:
                {
                    "Content-Type": "application/json"
                },
            body: req
        })
        .then(res => res.json())
        .then(data => console.log(data));
}
function read()
{
    const req = JSON.stringify(
        {
            name: "Jimmy",
            age: 18
        });
    Object.freeze(req);
    fetch("/users/find",
        {
            method: "POST",
            headers:
                {
                    "Content-Type": "application/json"
                },
            body: req
        })
        .then(res => res.json())
        .then(data => console.log(data));
}
function update()
{
    const req = JSON.stringify(
        {
            name: "Jimmy",
            age: 19
        });
    Object.freeze(req);
    fetch("/users/update",
        {
            method: "POST",
            headers:
                {
                    "Content-Type": "application/json"
                },
            body: req
        }).then(res => res.json())
        .then(data => console.log(data));
}
function del()
{
    const req = JSON.stringify(
        {
            name: "Jimmy",
            age: 18
        });
    Object.freeze(req);
    fetch("/users/delete",
        {
            method: "POST",
            headers:
                {
                    "Content-Type": "application/json"
                },
            body: req
        })
        .then(res => res.json())
        .then(data => console.log(data));
}