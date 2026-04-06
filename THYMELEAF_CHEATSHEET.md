# Thymeleaf Cheat Sheet

## Quick Start

Thymeleaf uses the `th:` namespace to add dynamic functionality to HTML. Always include the namespace in your template:

```html
<html xmlns:th="http://www.thymeleaf.org">
```

---

## Core Attributes

### Variable Expression: `th:text` and `th:utext`

Display model data in HTML elements.

```html
<!-- th:text = escapes HTML (safe) -->
<h1 th:text="${fighter.name}">Fighter Name</h1>

<!-- th:utext = renders raw HTML (use with caution) -->
<div th:utext="${description}">Description</div>
```

### Attribute Binding: `th:attr`

Bind model data to any HTML attribute.

```html
<!-- Single attribute -->
<img th:src="${fighter.imageUrl}" alt="Fighter">

<!-- Multiple attributes -->
<a th:attr="href=@{/fighters/{id}(id=${fighter.id})}" th:text="${fighter.name}">Link</a>

<!-- Shorthand for common attributes -->
<input th:value="${fighter.name}">
<button th:disabled="${fighter.isDisabled}">Click</button>
<a th:href="@{/home}">Home</a>
```

---

## Iteration: `th:each`

Loop through collections.

```html
<!-- Basic iteration -->
<tr th:each="fighter : ${fighters}">
  <td th:text="${fighter.name}">Name</td>
  <td th:text="${fighter.health}">Health</td>
</tr>

<!-- Access iteration status (index, count, first, last, etc.) -->
<div th:each="fighter, stat : ${fighters}">
  <span th:text="${stat.index}">0</span> <!-- 0-based index -->
  <span th:text="${stat.count}">1</span> <!-- 1-based count -->
  <span th:if="${stat.first}">First Item</span>
  <span th:if="${stat.last}">Last Item</span>
</div>
```

---

## Conditionals: `th:if`, `th:unless`

Display elements conditionally.

```html
<!-- Show if condition is true -->
<div th:if="${fighter.health > 1000}">
  <p>Strong fighter!</p>
</div>

<!-- Show if condition is false -->
<div th:unless="${fighter.isDefeat}">
  <p>Still fighting!</p>
</div>

<!-- Switch statement (multiple conditions) -->
<div th:switch="${fighter.tier}">
  <span th:case="'S'">S-Tier</span>
  <span th:case="'A'">A-Tier</span>
  <span th:case="*">Unknown</span> <!-- Default case -->
</div>
```

---

## URL Generation: `@{}`

Generate application URLs with parameters (prevents hardcoding).

```html
<!-- Simple path -->
<a th:href="@{/fighters}">View All Fighters</a>

<!-- With parameters -->
<a th:href="@{/fighters/{id}(id=${fighter.id})}">View Fighter</a>

<!-- Multiple parameters -->
<a th:href="@{/search(name=${fighter.name},tier=${fighter.tier})}">Search</a>

<!-- Path with query string -->
<a th:href="@{/fighters(page=2,sort='name')}">Next Page</a>
```

---

## Form Binding: `th:object`, `th:field`

Bind model objects to forms for two-way binding and error handling.

```html
<!-- Bind form to object -->
<form th:action="@{/fighters/save}" th:object="${fighterForm}" method="post">
  
  <!-- Simple field binding -->
  <input type="text" th:field="*{name}" placeholder="Fighter name">
  
  <!-- Error messages -->
  <span th:if="${#fields.hasErrors('name')}" 
        th:errors="*{name}" 
        class="error">Name error</span>
  
  <!-- Select dropdown -->
  <select th:field="*{tier}">
    <option th:each="t : ${tiers}" th:value="${t}" th:text="${t}">Select</option>
  </select>
  
  <!-- Checkbox -->
  <input type="checkbox" th:field="*{isActive}">
  
  <!-- Radio button -->
  <input type="radio" th:field="*{difficulty}" th:value="'Easy'">
  <input type="radio" th:field="*{difficulty}" th:value="'Hard'">
  
  <button type="submit">Save</button>
</form>
```

### Global Errors & Field Errors

```html
<!-- Display all validation errors -->
<div th:if="${#fields.hasErrors('*')}">
  <ul>
    <li th:each="err : ${#fields.errors('*')}" th:text="${err}">Error</li>
  </ul>
</div>

<!-- Display specific field errors -->
<div th:if="${#fields.hasErrors('health')}">
  <p th:errors="*{health}">Health error</p>
</div>
```

---

## Fragment Reusability: `th:insert`, `th:replace`

Reuse HTML snippets across templates.

```html
<!-- Define a fragment in navbar.html -->
<nav th:fragment="navbar">
  <ul>
    <li><a th:href="@{/home}">Home</a></li>
    <li><a th:href="@{/fighters}">Fighters</a></li>
  </ul>
</nav>

<!-- Include fragment in another template (inserts as child) -->
<div th:insert="navbar :: navbar"></div>

<!-- Replace fragment (replaces the element itself) -->
<div th:replace="navbar :: navbar"></div>

<!-- Simplified syntax (if fragment name matches file name) -->
<div th:replace="navbar"></div>
```

---

## Variables & Scope: `th:with`

Define local variables in templates.

```html
<!-- Single variable -->
<div th:with="maxHealth=1000">
  <span th:text="${fighter.health > maxHealth ? 'Over' : 'Under'}">Status</span>
</div>

<!-- Multiple variables -->
<div th:with="strength=${fighter.damage}, defense=${fighter.resistance}">
  <p th:text="${strength > defense ? 'Attacker' : 'Defender'}">Role</p>
</div>

<!-- In loops -->
<div th:each="fighter : ${fighters}" th:with="rating=${fighter.damage * fighter.health}">
  <p th:text="'Rating: ' + ${rating}">Rating</p>
</div>
```

---

## String Interpolation

Combine variables with text.

```html
<!-- Pipe syntax -->
<p th:text="|Welcome, ${user.name}!|">Welcome</p>

<!-- String concatenation -->
<p th:text="'Damage: ' + ${fighter.damage}">Damage</p>

<!-- In attributes -->
<a th:href="@{/fighters}" th:title="|Click to view ${fighter.name}|">Link</a>
```

---

## Utility Objects (Built-in)

Thymeleaf provides powerful utility objects for common tasks.

### `#strings` - String Utilities

```html
<!-- Check if empty/blank -->
<div th:if="${#strings.isEmpty(fighter.name)}">
  <p>Name is empty</p>
</div>

<!-- String operations -->
<p th:text="${#strings.toUpperCase(fighter.name)}">Name</p>
<p th:text="${#strings.toLowerCase(fighter.name)}">Name</p>
<p th:text="${#strings.length(fighter.name)}">Length</p>
<p th:text="${#strings.substring(fighter.name, 0, 3)}">Substring</p>
<p th:if="${#strings.contains(fighter.name, 'Kazuya')}">Contains</p>
```

### `#numbers` - Number Formatting

```html
<!-- Format numbers -->
<p th:text="${#numbers.formatDecimal(fighter.health, 1, 2)}">123.45</p>
<p th:text="${#numbers.formatInteger(fighter.damage, 2)}">00</p>
```

### `#dates` - Date Formatting

```html
<!-- Format dates -->
<p th:text="${#dates.format(fighter.createdAt, 'yyyy-MM-dd')}">Date</p>
<p th:text="${#dates.format(fighter.createdAt, 'dd/MM/yyyy HH:mm')}">DateTime</p>
```

### `#lists` - List/Collection Utilities

```html
<!-- Check if list is empty -->
<div th:if="${#lists.isEmpty(fighters)}">
  <p>No fighters found</p>
</div>

<!-- Get list size -->
<p th:text="${#lists.size(fighters)}">Count</p>
```

### `#fields` - Form Field Utilities

```html
<!-- Check if field has errors -->
<span th:if="${#fields.hasErrors('name')}" class="error">Invalid</span>

<!-- Get all errors for a field -->
<p th:if="${#fields.hasAnyErrors()}" class="error">Form has errors</p>
```

---

## CSS & Class Management

### `th:class` - Dynamic CSS Classes

```html
<!-- Conditional class -->
<div th:classappend="${fighter.health > 1000 ? 'strong' : 'weak'}">
  Fighter
</div>

<!-- Multiple classes -->
<div th:class="${fighter.isActive ? 'active' : 'inactive'} + ' fighter'">
  Status
</div>

<!-- Using object -->
<div th:class="@{${fighter.tier}=='S' ? 'tier-s' : 'tier-a'}">
  Tier
</div>
```

### `th:style` - Dynamic Inline Styles

```html
<!-- Conditional style -->
<div th:style="${fighter.health < 500 ? 'color: red' : 'color: green'}">
  Health
</div>

<!-- Multiple styles -->
<p th:style="'color:' + ${fighter.isActive ? 'green' : 'gray'} + '; font-weight: bold;'">
  Status
</p>
```

---

## Operators & Expressions

### Comparison & Logical Operators

```html
<!-- Comparison -->
<span th:if="${fighter.health > 1000}">Strong</span>
<span th:if="${fighter.damage < 100}">Weak Attacker</span>
<span th:if="${fighter.tier == 'S'}">Top Tier</span>
<span th:if="${fighter.name != 'Default'}">Named</span>

<!-- Logical operators -->
<span th:if="${fighter.health > 1000 and fighter.damage > 50}">Powerful</span>
<span th:if="${fighter.tier == 'S' or fighter.tier == 'A'}">High Tier</span>
<span th:if="${!fighter.isDefeat}">Winning</span>
```

### Ternary Operator

```html
<p th:text="${fighter.health > 1000 ? 'Strong' : 'Weak'}">Status</p>

<!-- Elvis operator (simpler syntax) -->
<p th:text="${fighter.name ?: 'Unknown'}">Name or default</p>
```

---

## Comments

```html
<!-- Standard HTML comment (visible in page source) -->
<!-- This is visible -->

<!-- Thymeleaf comment (removed during processing) -->
<!--/* This comment is removed by Thymeleaf */-->

<!-- Multi-line Thymeleaf comment -->
<!--/*
<div th:each="fighter : ${fighters}">
  This code won't render
</div>
*/-->
```

---

## Common Patterns

### Display & Default Values

```html
<!-- Show value or default text -->
<p th:text="${fighter.nickname} ?: 'No nickname'">Nickname</p>

<!-- Show if exists, hide if null -->
<p th:if="${fighter.description}" th:text="${fighter.description}"></p>
```

### Conditional Display with Bootstrap Classes

```html
<div th:class="'alert ' + ${error ? 'alert-danger' : 'alert-success'}">
  <span th:text="${error ?: 'Success!'}">Message</span>
</div>
```

### Form Errors with Styling

```html
<div class="form-group">
  <label th:for="name">Fighter Name</label>
  <input type="text" 
         th:field="*{name}" 
         th:class="${#fields.hasErrors('name') ? 'form-control is-invalid' : 'form-control'}"
         class="form-control">
```


## Tips & Best Practices

1. **Always escape user input** - Use `th:text` instead of `th:utext` unless you specifically need raw HTML
2. **Use `@{}` for URLs** - Never hardcode paths; use `@{}` for flexibility
3. **Leverage fragments** - Extract common UI patterns into reusable fragments
4. **Use `th:object` for forms** - Makes binding cleaner and enables error handling
5. **Prefer `th:classappend`** - When adding classes conditionally to existing ones
6. **Use utility objects** - `#strings`, `#numbers`, `#dates`, etc. reduce Java logic in templates
7. **Keep templates simple** - Complex logic belongs in controllers, not templates