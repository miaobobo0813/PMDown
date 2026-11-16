# PMDown

A customized Markdown interpreter that converts a Markdown file into an HTML file.

## Toolchain

JDK 27

## Supported Markdown Syntax

1. Use `\` to escape the following character, rendering it as plain text.
2. Use `#`, `##`, `###`, etc. to define headings (H1, H2, H3, etc.).
3. Use `[Display Text](URL)` to create a link.
4. Use `*Text*` for italic text.
5. Use `**Text**` for bold text.
6. Use `~~Text~~` for strikethrough text.
7. Use `$formula$` for inline math formulas.
8. Tables, lists: see examples below.
9. Paragraphs must be separated by a blank line.

> [!NOTE]
> 1. Links only support the `http:`, `https:`, and `mailto:` protocols. Unsupported protocols will be rendered as plain text.
> 2. Math formulas are rendered using MathJax. For formula syntax, please visit the [MathJax documentation](https://docs.mathjax.org/en/latest/input/tex/index.html). The `$formula$` syntax in Markdown is converted to the `\(formula\)` delimiter format used by MathJax.

## Examples

```markdown
# Example

This is an example.

This is the first line.  
This line will appear on the same line as the previous one.

This line will appear on a new line.

Table examples:

| Title 1 | Title 2 |
| Content for Title 1 |
| --- | --- |
| The separator must contain just right three hyphens (`---`). Missing cells are rendered as empty; extra cells are ignored. | This cell is valid | Extra cells like this one are ignored |
| The cell to the right of this one is empty |
| --- |
| Content after the second separator row is ignored. |

Table without separator row:
| Title   | Title   |
| Content | Content |
| All rows in this table are rendered as plain text because no separator row is present. | All rows in this table are rendered as plain text because no separator row is present. |

An ordered list:
1.You can start an ordered list with the `1.` prefix.
You do not need to write `2.`, `3.`, and so on on the following lines.
Each line is treated as one list item.
To end the list, start a new paragraph.

Like this.

An unordered list:
-.You can start an unordered list with the `-.` prefix.
The other rules are the same as for ordered lists.
```

## License

MIT License, see [LICENSE](LICENSE.md)
