function ProfileCard({ user }) {
  return (
    <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl max-w-xl">
      <p className="mb-3">
        <span className="text-slate-400">User ID:</span> {user.id}
      </p>

      <p className="mb-3">
        <span className="text-slate-400">Username:</span> {user.username}
      </p>

      <p>
        <span className="text-slate-400">Role:</span> {user.role}
      </p>
    </div>
  );
}

export default ProfileCard;
