import { useEffect, useState } from "react";
import API from "../../api/api";
import {
  Users,
  Mail,
  Phone,
  User,
  Trash2,
  Search,
  Loader2,
  UserPlus,
} from "lucide-react";

function AdminMembersPage() {
  const [members, setMembers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");

  async function loadMembers() {
    try {
      setLoading(true);
      const response = await API.get("/members");
      setMembers(response.data || []);
    } catch (err) {
      console.error("Failed to load members:", err);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadMembers();
  }, []);

  const handleDelete = async (memberId) => {
    const confirmed = window.confirm(
      "Are you sure you want to delete this member?",
    );
    if (!confirmed) return;

    try {
      await API.delete(`/members?id=${memberId}`);
      await loadMembers();
    } catch (err) {
      alert(err.response?.data?.error || "Delete failed");
    }
  };

  const filteredMembers = members.filter(
    (m) =>
      m.username.toLowerCase().includes(searchQuery.toLowerCase()) ||
      m.email?.toLowerCase().includes(searchQuery.toLowerCase()),
  );

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center h-64 text-slate-400">
        <Loader2 className="animate-spin mb-4" size={32} />
        <p className="text-lg">Fetching members...</p>
      </div>
    );
  }

  return (
    <div className="max-w-6xl mx-auto">
      <div className="flex flex-col md:flex-row md:items-end justify-between gap-6 mb-10">
        <div>
          <h2 className="text-4xl font-extrabold text-white tracking-tight">
            Manage Members
          </h2>
          <p className="text-slate-400 mt-2 flex items-center gap-2">
            <Users size={18} className="text-indigo-400" />
            Administration portal for library user accounts.
          </p>
        </div>

        <div className="bg-slate-900 border border-slate-800 px-6 py-3 rounded-2xl flex flex-col items-center">
          <span className="text-xs font-bold text-slate-500 uppercase tracking-wider">
            Total Members
          </span>
          <span className="text-2xl font-black text-indigo-400">
            {members.length}
          </span>
        </div>
      </div>

      <div className="relative mb-8">
        <Search
          className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-500"
          size={20}
        />
        <input
          type="text"
          placeholder="Search by name or email..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          className="w-full bg-slate-900 border border-slate-800 rounded-2xl py-4 pl-12 pr-4 text-white focus:outline-none focus:ring-2 focus:ring-indigo-500/50 transition-all"
        />
      </div>

      <div className="grid gap-4">
        {filteredMembers.length === 0 ? (
          <div className="bg-slate-900/50 border border-dashed border-slate-800 rounded-3xl p-12 text-center">
            <UserPlus className="text-slate-500 mx-auto mb-4" size={32} />
            <p className="text-slate-400 text-lg">No members found.</p>
          </div>
        ) : (
          filteredMembers.map((member) => {
            // DEBUG: This will print every member's data to your console
            console.log("Member Data:", member);

            // Check both common naming conventions for roles
            const userRole = member.role || member.memberType || "MEMBER";

            return (
              <div
                key={member.id}
                className="group bg-slate-900 border border-slate-800 hover:border-indigo-500/30 p-5 rounded-2xl flex flex-col md:flex-row md:items-center justify-between gap-6 transition-all duration-300"
              >
                <div className="flex items-start gap-4">
                  <div className="h-12 w-12 bg-slate-800 rounded-xl flex items-center justify-center text-indigo-400 group-hover:bg-indigo-600 group-hover:text-white transition-colors">
                    <User size={24} />
                  </div>
                  <div>
                    <div className="flex items-center gap-3">
                      <h3 className="text-lg font-bold text-white uppercase tracking-wide">
                        {member.username}
                      </h3>
                      <span
                        className={`px-2 py-0.5 rounded text-[10px] font-bold uppercase tracking-widest ${
                          userRole === "ADMIN"
                            ? "bg-amber-500/10 text-amber-500 border border-amber-500/20"
                            : "bg-indigo-500/10 text-indigo-400 border border-indigo-500/20"
                        }`}
                      >
                        {userRole}
                      </span>
                    </div>

                    <div className="mt-3 flex flex-wrap gap-4 text-sm text-slate-400">
                      <span className="flex items-center gap-2">
                        <Mail size={14} className="text-slate-600" />
                        {member.email || "No email"}
                      </span>
                      <span className="flex items-center gap-2">
                        <Phone size={14} className="text-slate-600" />
                        {member.phone || "No phone"}
                      </span>
                    </div>
                  </div>
                </div>

                <button
                  onClick={() => handleDelete(member.id)}
                  className="flex items-center gap-2 bg-slate-800 hover:bg-red-600 text-slate-300 hover:text-white px-5 py-2.5 rounded-xl transition-all duration-200 font-semibold text-sm group/btn"
                >
                  <Trash2
                    size={16}
                    className="text-slate-500 group-hover/btn:text-white"
                  />
                  Remove Account
                </button>
              </div>
            );
          })
        )}
      </div>
    </div>
  );
}

export default AdminMembersPage;
