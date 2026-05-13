import { useState } from "react";
import API from "../../api/api";

function AdminBooksPage({ books, reload }) {
  const [searchTerm, setSearchTerm] = useState("");
  const [categoryFilter, setCategoryFilter] = useState("All");

  const [form, setForm] = useState({
    title: "",
    author: "",
    category: "",
    isbn: "",
    status: "AVAILABLE",
  });

  const [editingBook, setEditingBook] = useState(null);

  const handleChange = (e) => {
    setForm((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const resetForm = () => {
    setForm({
      title: "",
      author: "",
      category: "",
      isbn: "",
      status: "AVAILABLE",
    });
    setEditingBook(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      if (editingBook) {
        await API.put("/books", {
          id: editingBook.id,
          ...form,
        });

        alert("Book updated successfully");
      } else {
        await API.post("/books", form);
        alert("Book added successfully");
      }

      resetForm();
      await reload();
    } catch (err) {
      alert(err.response?.data?.error || "Operation failed");
    }
  };

  const filteredBooks = books.filter((book) => {
    const matchesSearch =
      book.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
      book.author.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesCategory =
      categoryFilter === "All" || book.category === categoryFilter;
    return matchesSearch && matchesCategory;
  });

  const categories = ["All", ...new Set(books.map((b) => b.category))].filter(
    (c) => c,
  );

  const handleEdit = (book) => {
    setEditingBook(book);

    setForm({
      title: book.title || "",
      author: book.author || "",
      category: book.category || "",
      isbn: book.isbn || "",
      status: book.status || "AVAILABLE",
    });
  };

  const handleDelete = async (id) => {
    if (!confirm("Are you sure you want to delete this book?")) return;

    try {
      await API.delete(`/books?id=${id}`);
      alert("Book deleted successfully");
      await reload();
    } catch (err) {
      alert(err.response?.data?.error || "Delete failed");
    }
  };

  return (
    <>
      <h2 className="text-2xl font-bold mb-5">Manage Books</h2>

      <form
        onSubmit={handleSubmit}
        className="bg-slate-900 border border-slate-800 p-6 rounded-2xl mb-8 grid grid-cols-1 md:grid-cols-2 gap-4"
      >
        <input
          name="title"
          value={form.title}
          onChange={handleChange}
          placeholder="Book title"
          className="bg-slate-950 border border-slate-700 rounded-lg px-4 py-3"
          required
        />

        <input
          name="author"
          value={form.author}
          onChange={handleChange}
          placeholder="Author"
          className="bg-slate-950 border border-slate-700 rounded-lg px-4 py-3"
          required
        />

        <input
          name="category"
          value={form.category}
          onChange={handleChange}
          placeholder="Category"
          className="bg-slate-950 border border-slate-700 rounded-lg px-4 py-3"
        />

        <input
          name="isbn"
          value={form.isbn}
          onChange={handleChange}
          placeholder="ISBN"
          className="bg-slate-950 border border-slate-700 rounded-lg px-4 py-3"
        />

        <select
          name="status"
          value={form.status}
          onChange={handleChange}
          className="bg-slate-950 border border-slate-700 rounded-lg px-4 py-3"
        >
          <option value="AVAILABLE">AVAILABLE</option>
          <option value="BORROWED">BORROWED</option>
          <option value="RESERVED">RESERVED</option>
        </select>

        <div className="flex gap-3">
          <button className="bg-indigo-600 hover:bg-indigo-700 px-5 py-3 rounded-lg">
            {editingBook ? "Update Book" : "Add Book"}
          </button>

          {editingBook && (
            <button
              type="button"
              onClick={resetForm}
              className="bg-slate-700 hover:bg-slate-600 px-5 py-3 rounded-lg"
            >
              Cancel
            </button>
          )}
        </div>
      </form>

      <div className="flex flex-col md:flex-row gap-4 mb-6">
        <input
          type="text"
          placeholder="Search by title or author..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="flex-1 bg-slate-900 border border-slate-800 rounded-lg px-4 py-2 text-white"
        />
        <select
          value={categoryFilter}
          onChange={(e) => setCategoryFilter(e.target.value)}
          className="bg-slate-900 border border-slate-800 rounded-lg px-4 py-2 text-white"
        >
          {categories.map((c) => (
            <option key={c} value={c}>
              {c}
            </option>
          ))}
        </select>
      </div>

      <div className="bg-slate-900 border border-slate-800 rounded-2xl overflow-hidden">
        {filteredBooks.length === 0 ? (
          <div className="p-10 text-center text-slate-500">No books found.</div>
        ) : (
          filteredBooks.map((book) => (
            <div
              key={book.id}
              className="p-5 border-b border-slate-800 flex justify-between items-center gap-4"
            >
              <div>
                <h3 className="font-bold">{book.title}</h3>
                <p className="text-slate-400 text-sm">
                  {book.author} • {book.category} • {book.status}
                </p>
              </div>

              <div className="flex gap-2">
                <button
                  onClick={() => handleEdit(book)}
                  className="bg-yellow-600 hover:bg-yellow-700 px-4 py-2 rounded-lg text-sm"
                >
                  Edit
                </button>

                <button
                  onClick={() => handleDelete(book.id)}
                  className="bg-red-600 hover:bg-red-700 px-4 py-2 rounded-lg text-sm"
                >
                  Delete
                </button>
              </div>
            </div>
          ))
        )}
      </div>
    </>
  );
}

export default AdminBooksPage;
