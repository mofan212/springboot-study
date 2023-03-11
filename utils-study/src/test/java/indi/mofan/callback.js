function sum(a, b) {
    return a + b;
}

function subtract(a, b) {
    return a - b;
}

function print(a) {
    console.log("计算结果是: " + a)
}

print(sum(1, 2));
print(subtract(2, 1));

function print_with_fun(fun, a, b) {
    console.log("计算结果是: " + fun(a, b));
}

print_with_fun(sum, 1, 2);
print_with_fun(subtract, 2, 1);

print_with_fun((a, b) => {
    return (a + b) / 2;
}, 2, 2)

function consumer(str) {
    console.log(str);
}

function nestedConsumer(fun) {
    fun("hello lambda safe");
}

nestedConsumer(consumer);