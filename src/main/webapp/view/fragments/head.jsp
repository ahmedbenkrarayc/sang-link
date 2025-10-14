<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Donor Registration | SangLink</title>
<script src="https://cdn.tailwindcss.com"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<script>
    tailwind.config = {
        theme: {
            extend: {
                colors: {
                    primary: '#3b82f6',
                    secondary: '#1e40af',
                    accent: '#ef4444'
                }
            }
        }
    }

    function clearFormOnSuccess() {
        const successAlert = document.getElementById('successAlert');
        if (successAlert && !successAlert.classList.contains('hidden')) {
            document.getElementById('donorForm').reset();
        }
    }

    document.addEventListener('DOMContentLoaded', function() {
        clearFormOnSuccess();
    });
</script>