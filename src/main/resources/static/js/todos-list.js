document.addEventListener("DOMContentLoaded", function () {
  const buttons = document.querySelectorAll('[data-toggle="detail"]');
  buttons.forEach((btn) => {
    const targetId = btn.getAttribute("data-target");
    const target = document.getElementById(targetId);
    if (!target) return;

    btn.addEventListener("click", () => {
      const isHidden = target.classList.contains("d-none");
      target.classList.toggle("d-none", !isHidden);
      btn.textContent = isHidden ? "- 閉じる" : "+ 詳細";
    });
  });
});
