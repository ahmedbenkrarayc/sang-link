<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../fragments/head.jsp"/>
    <style>
        .donor-card {
            background: white;
            border-radius: 16px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
            border: 1px solid #e5e7eb;
            transition: all 0.3s ease;
            overflow: hidden;
            position: relative;
        }

        .donor-card:hover {
            transform: translateY(-4px);
            box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
            border-color: #3b82f6;
        }

        .card-header {
            background: linear-gradient(135deg, #3b82f6, #1e40af);
            padding: 24px 20px 20px 20px;
            color: white;
            position: relative;
        }

        .blood-badge {
            position: absolute;
            top: 20px;
            right: 20px;
            width: 60px;
            height: 60px;
            background: white;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
            font-size: 18px;
            color: #ef4444;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
            border: 3px solid #fef2f2;
        }

        .donor-name {
            font-size: 20px;
            font-weight: 700;
            margin-bottom: 4px;
        }

        .donor-cin {
            font-size: 14px;
            opacity: 0.9;
            font-weight: 500;
        }

        .status-badge {
            display: inline-flex;
            align-items: center;
            padding: 6px 16px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            margin-top: 12px;
            background: rgba(255, 255, 255, 0.2);
            color: white;
            backdrop-filter: blur(10px);
        }

        .card-body {
            padding: 24px 20px 20px 20px;
        }

        .info-grid {
            display: grid;
            gap: 16px;
            margin-bottom: 20px;
        }

        .info-item {
            display: flex;
            align-items: center;
            padding: 12px;
            background: #f8fafc;
            border-radius: 12px;
            border: 1px solid #f1f5f9;
            transition: all 0.2s ease;
        }

        .info-item:hover {
            background: #f1f5f9;
            transform: translateX(4px);
        }

        .info-item i {
            width: 20px;
            margin-right: 12px;
            color: #3b82f6;
            font-size: 14px;
        }

        .info-text {
            font-size: 14px;
            color: #374151;
            font-weight: 500;
        }

        .last-donation {
            background: linear-gradient(135deg, #ecfdf5, #d1fae5);
            border: 1px solid #a7f3d0;
            border-radius: 12px;
            padding: 16px;
            margin: 20px 0;
            color: #065f46;
            font-size: 14px;
            font-weight: 600;
            display: flex;
            align-items: center;
        }

        .last-donation i {
            color: #10b981;
            margin-right: 12px;
            font-size: 16px;
        }

        .action-buttons {
            display: grid;
            grid-template-columns: 1fr 1fr 1fr;
            gap: 10px;
        }

        .action-btn {
            padding: 12px 8px;
            border: none;
            border-radius: 10px;
            font-size: 12px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 6px;
        }

        .action-btn i {
            font-size: 16px;
            margin-bottom: 2px;
        }

        .action-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }

        .btn-edit {
            background: #dbeafe;
            color: #1e40af;
            border: 1px solid #bfdbfe;
        }

        .btn-edit:hover {
            background: #1e40af;
            color: white;
        }

        .btn-view {
            background: #dcfce7;
            color: #166534;
            border: 1px solid #bbf7d0;
        }

        .btn-view:hover {
            background: #166534;
            color: white;
        }

        .btn-delete {
            background: #fef2f2;
            color: #dc2626;
            border: 1px solid #fecaca;
        }

        .btn-delete:hover {
            background: #dc2626;
            color: white;
        }

        /* Modal Styles */
        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            backdrop-filter: blur(5px);
            z-index: 1000;
            align-items: center;
            justify-content: center;
        }

        .modal-content {
            background: white;
            border-radius: 20px;
            box-shadow: 0 25px 50px rgba(0, 0, 0, 0.2);
            width: 90%;
            max-width: 500px;
            max-height: 90vh;
            overflow-y: auto;
            animation: modalSlideIn 0.3s ease-out;
        }

        @keyframes modalSlideIn {
            from {
                opacity: 0;
                transform: translateY(-50px) scale(0.9);
            }
            to {
                opacity: 1;
                transform: translateY(0) scale(1);
            }
        }

        .modal-header {
            background: linear-gradient(135deg, #3b82f6, #1e40af);
            padding: 24px;
            color: white;
            border-radius: 20px 20px 0 0;
            position: relative;
        }

        .modal-title {
            font-size: 24px;
            font-weight: 700;
            margin-bottom: 8px;
        }

        .modal-subtitle {
            font-size: 14px;
            opacity: 0.9;
        }

        .close-btn {
            position: absolute;
            top: 20px;
            right: 20px;
            background: rgba(255, 255, 255, 0.2);
            border: none;
            color: white;
            width: 32px;
            height: 32px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            transition: all 0.2s ease;
        }

        .close-btn:hover {
            background: rgba(255, 255, 255, 0.3);
            transform: rotate(90deg);
        }

        .modal-body {
            padding: 24px;
        }

        .assessment-date {
            background: #f8fafc;
            border: 1px solid #e5e7eb;
            border-radius: 12px;
            padding: 16px;
            margin-bottom: 24px;
            text-align: center;
        }

        .date-label {
            font-size: 12px;
            color: #6b7280;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            margin-bottom: 4px;
        }

        .date-value {
            font-size: 16px;
            font-weight: 700;
            color: #1f2937;
        }

        .medical-grid {
            display: grid;
            gap: 12px;
        }

        .medical-item {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 16px;
            background: #f8fafc;
            border-radius: 12px;
            border: 1px solid #e5e7eb;
            transition: all 0.2s ease;
        }

        .medical-item:hover {
            background: #f1f5f9;
            transform: translateX(4px);
        }

        .condition-name {
            font-size: 14px;
            font-weight: 600;
            color: #374151;
        }

        .condition-status {
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 700;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .status-positive {
            background: #fef2f2;
            color: #dc2626;
        }

        .status-negative {
            background: #f0fdf4;
            color: #16a34a;
        }

        .no-assessment {
            text-align: center;
            padding: 40px 20px;
            color: #6b7280;
        }

        .no-assessment i {
            font-size: 48px;
            margin-bottom: 16px;
            opacity: 0.5;
        }
    </style>
</head>
<body class="bg-gray-50 min-h-screen">
<jsp:include page="../fragments/nav.jsp"/>

<!-- Medical Assessment Modal -->
<div id="medicalModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <button class="close-btn" onclick="closeModal()">
                <i class="fas fa-times"></i>
            </button>
            <div class="modal-title">Medical Assessment</div>
            <div class="modal-subtitle" id="modalDonorName"></div>
        </div>
        <div class="modal-body">
            <div id="assessmentContent">

            </div>
        </div>
    </div>
</div>

<div class="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
    <div class="mb-8">
        <div class="flex justify-between items-center">
            <div>
                <h1 class="text-3xl font-bold text-gray-900">Donor Management</h1>
                <p class="text-gray-600 mt-2">Manage your life-saving donor community</p>
            </div>
            <a href="${pageContext.request.contextPath}/donor"
               class="inline-flex items-center px-6 py-3 bg-primary text-white font-semibold rounded-xl hover:bg-secondary transition-all duration-200 hover:scale-105 shadow-lg">
                <i class="fas fa-plus mr-2"></i> Add New Donor
            </a>
        </div>
    </div>

    <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6 mb-8">
        <form method="GET" class="space-y-4 md:space-y-0 md:flex md:items-end md:space-x-6">
            <div class="flex-1 grid grid-cols-1 md:grid-cols-3 gap-6">
                <div>
                    <label class="block text-sm font-semibold text-gray-700 mb-2">Search Donors</label>
                    <div class="relative">
                        <i class="fas fa-search absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
                        <input type="text" name="search" value="${param.search}"
                               placeholder="Name, CIN, phone..."
                               class="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-primary focus:border-transparent">
                    </div>
                </div>

                <div>
                    <label class="block text-sm font-semibold text-gray-700 mb-2">Filter by Status</label>
                    <select name="status" class="w-full px-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-primary focus:border-transparent">
                        <option value="">All Statuses</option>
                        <c:forEach var="status" items="${statuses}">
                            <option value="${status}" ${param.status == status ? 'selected' : ''}>${status}</option>
                        </c:forEach>
                    </select>
                </div>

                <div>
                    <label class="block text-sm font-semibold text-gray-700 mb-2">Items per page</label>
                    <select name="pageSize" class="w-full px-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-primary focus:border-transparent">
                        <option value="5" ${param.pageSize == '5' or empty param.pageSize ? 'selected' : ''}>5 donors</option>
                        <option value="10" ${param.pageSize == '10' ? 'selected' : ''}>10 donors</option>
                        <option value="25" ${param.pageSize == '25' ? 'selected' : ''}>25 donors</option>
                        <option value="50" ${param.pageSize == '50' ? 'selected' : ''}>50 donors</option>
                        <option value="75" ${param.pageSize == '75' ? 'selected' : ''}>75 donors</option>
                        <option value="100" ${param.pageSize == '100' ? 'selected' : ''}>100 donors</option>
                    </select>
                </div>
            </div>

            <div class="flex space-x-3">
                <button type="submit"
                        class="flex items-center px-6 py-3 bg-primary text-white rounded-xl hover:bg-secondary transition-colors shadow-lg">
                    <i class="fas fa-filter mr-2"></i> Apply Filters
                </button>
                <a href="${pageContext.request.contextPath}/donor"
                   class="flex items-center px-6 py-3 border border-gray-300 text-gray-700 rounded-xl hover:bg-gray-50 transition-colors">
                    <i class="fas fa-refresh mr-2"></i> Reset
                </a>
            </div>
        </form>
    </div>

    <c:choose>
        <c:when test="${not empty donors}">
            <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 2xl:grid-cols-4 gap-8 mb-8">
                <c:forEach var="donor" items="${donors}">
                    <div class="donor-card">
                        <div class="card-header">
                            <div class="blood-badge">
                                <c:choose>
                                    <c:when test="${donor.bloodGroup == 'A_POSITIVE'}">A+</c:when>
                                    <c:when test="${donor.bloodGroup == 'A_NEGATIVE'}">A-</c:when>
                                    <c:when test="${donor.bloodGroup == 'B_POSITIVE'}">B+</c:when>
                                    <c:when test="${donor.bloodGroup == 'B_NEGATIVE'}">B-</c:when>
                                    <c:when test="${donor.bloodGroup == 'AB_POSITIVE'}">AB+</c:when>
                                    <c:when test="${donor.bloodGroup == 'AB_NEGATIVE'}">AB-</c:when>
                                    <c:when test="${donor.bloodGroup == 'O_POSITIVE'}">O+</c:when>
                                    <c:when test="${donor.bloodGroup == 'O_NEGATIVE'}">O-</c:when>
                                    <c:otherwise>${donor.bloodGroup}</c:otherwise>
                                </c:choose>
                            </div>
                            <div class="donor-name">${donor.prenom} ${donor.nom}</div>
                            <div class="donor-cin">${donor.cin}</div>
                            <div class="status-badge">
                                    ${donor.status}
                            </div>
                        </div>

                        <div class="card-body">
                            <div class="info-grid">
                                <div class="info-item">
                                    <i class="fas fa-mobile-alt"></i>
                                    <span class="info-text">${donor.phone}</span>
                                </div>
                                <div class="info-item">
                                    <i class="fas fa-venus-mars"></i>
                                    <span class="info-text">${donor.gender}</span>
                                </div>
                                <div class="info-item">
                                    <i class="fas fa-birthday-cake"></i>
                                    <span class="info-text">${donor.birthday}</span>
                                </div>
                                <div class="info-item">
                                    <i class="fas fa-weight"></i>
                                    <span class="info-text">${donor.weight} kg</span>
                                </div>
                            </div>

                            <c:if test="${not empty donor.lastDonation}">
                                <div class="last-donation">
                                    <i class="fas fa-heart"></i>
                                    Last donation: ${donor.lastDonation}
                                </div>
                            </c:if>

                            <div class="action-buttons">
                                <button onclick="editDonor(${donor.id})"
                                        class="action-btn btn-edit">
                                    <i class="fas fa-edit"></i>
                                    <span>Edit</span>
                                </button>
                                <c:choose>
                                    <c:when test="${not empty donor.medicalAssessments and not empty donor.medicalAssessments[0]}">
                                        <c:set var="assessment" value="${donor.medicalAssessments[0]}" />
                                        <button onclick="showMedicalAssessment(
                                                '${donor.prenom} ${donor.nom}',
                                                '${assessment.assessmentDate}',
                                            ${assessment.hepatiteB},
                                            ${assessment.hepatiteC},
                                            ${assessment.vih},
                                            ${assessment.diabeteInsulin},
                                            ${assessment.grossesse},
                                            ${assessment.allaitement}
                                                )" class="action-btn btn-view">
                                            <i class="fas fa-eye"></i>
                                            <span>View</span>
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <button onclick="showMedicalAssessment('${donor.prenom} ${donor.nom}')"
                                                class="action-btn btn-view">
                                            <i class="fas fa-eye"></i>
                                            <span>View</span>
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                                <button onclick="deleteDonor(${donor.id}, '${donor.prenom} ${donor.nom}')"
                                        class="action-btn btn-delete">
                                    <i class="fas fa-trash"></i>
                                    <span>Delete</span>
                                </button>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <c:if test="${totalPages > 1}">
                <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
                    <div class="flex flex-col sm:flex-row items-center justify-between space-y-4 sm:space-y-0">
                        <div class="text-sm text-gray-600">
                            Page <span class="font-semibold text-gray-900">${currentPage}</span> of
                            <span class="font-semibold text-gray-900">${totalPages}</span> •
                            <span class="font-semibold text-primary">${total}</span> total donors
                        </div>
                        <div class="flex items-center space-x-2">
                            <c:if test="${currentPage > 1}">
                                <a href="?page=${currentPage - 1}&pageSize=${param.pageSize}&search=${param.search}&status=${param.status}"
                                   class="inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-xl text-gray-600 bg-white hover:bg-gray-50 transition duration-200">
                                    <i class="fas fa-chevron-left mr-2"></i> Previous
                                </a>
                            </c:if>

                            <div class="flex space-x-1">
                                <c:forEach begin="1" end="${totalPages}" var="pageNum">
                                    <c:choose>
                                        <c:when test="${pageNum == currentPage}">
                                                <span class="inline-flex items-center px-4 py-2 border border-primary text-sm font-medium rounded-xl text-white bg-primary">
                                                        ${pageNum}
                                                </span>
                                        </c:when>
                                        <c:when test="${pageNum >= currentPage - 2 && pageNum <= currentPage + 2}">
                                            <a href="?page=${pageNum}&pageSize=${param.pageSize}&search=${param.search}&status=${param.status}"
                                               class="inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-xl text-gray-600 bg-white hover:bg-gray-50 transition duration-200">
                                                    ${pageNum}
                                            </a>
                                        </c:when>
                                    </c:choose>
                                </c:forEach>
                            </div>

                            <c:if test="${currentPage < totalPages}">
                                <a href="?page=${currentPage + 1}&pageSize=${param.pageSize}&search=${param.search}&status=${param.status}"
                                   class="inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-xl text-gray-600 bg-white hover:bg-gray-50 transition duration-200">
                                    Next <i class="fas fa-chevron-right ml-2"></i>
                                </a>
                            </c:if>
                        </div>
                    </div>
                </div>
            </c:if>
        </c:when>

        <c:otherwise>
            <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-16 text-center">
                <div class="max-w-md mx-auto">
                    <div class="w-20 h-20 mx-auto mb-6 bg-gray-100 rounded-2xl flex items-center justify-center">
                        <i class="fas fa-users text-gray-400 text-2xl"></i>
                    </div>
                    <h3 class="text-2xl font-bold text-gray-900 mb-3">No donors found</h3>
                    <p class="text-gray-500 mb-8 text-lg">
                        <c:choose>
                            <c:when test="${not empty param.search or not empty param.status}">
                                No donors match your search criteria. Try adjusting your filters.
                            </c:when>
                            <c:otherwise>
                                Start building your donor community by adding the first donor.
                            </c:otherwise>
                        </c:choose>
                    </p>
                    <a href="${pageContext.request.contextPath}/donor"
                       class="inline-flex items-center px-8 py-4 bg-primary text-white font-semibold rounded-xl hover:bg-secondary transform hover:scale-105 transition duration-200 shadow-lg">
                        <i class="fas fa-plus mr-3"></i> Add First Donor
                    </a>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="../fragments/footer.jsp"/>

<script>
    function editDonor(donorId) {
        window.location.href = '${pageContext.request.contextPath}/donor/edit?id=' + donorId;
    }

    function showMedicalAssessment(donorName, assessmentDate, hepatiteB, hepatiteC, vih, diabeteInsulin, grossesse, allaitement) {
        document.getElementById('modalDonorName').textContent = donorName;

        if (!assessmentDate) {
            document.getElementById('assessmentContent').innerHTML = `
                    <div class="no-assessment">
                        <i class="fas fa-file-medical"></i>
                        <div>No medical assessment found</div>
                        <p class="text-sm mt-2 text-gray-500">This donor doesn't have any medical assessments yet.</p>
                    </div>
                `;
        } else {
            let html = `
                    <div class="assessment-date">
                        <div class="date-label">Assessment Date</div>
                        <div class="date-value">` + assessmentDate + `</div>
                    </div>
                    <div class="medical-grid">
                        <div class="medical-item">
                            <span class="condition-name">Hepatitis B</span>
                            <span class="condition-status ` + (hepatiteB ? 'status-positive' : 'status-negative') + `">
                                ` + (hepatiteB ? 'POSITIVE' : 'NEGATIVE') + `
                            </span>
                        </div>
                        <div class="medical-item">
                            <span class="condition-name">Hepatitis C</span>
                            <span class="condition-status ` + (hepatiteC ? 'status-positive' : 'status-negative') + `">
                                ` + (hepatiteC ? 'POSITIVE' : 'NEGATIVE') + `
                            </span>
                        </div>
                        <div class="medical-item">
                            <span class="condition-name">HIV</span>
                            <span class="condition-status ` + (vih ? 'status-positive' : 'status-negative') + `">
                                ` + (vih ? 'POSITIVE' : 'NEGATIVE') + `
                            </span>
                        </div>
                        <div class="medical-item">
                            <span class="condition-name">Diabetes (Insulin)</span>
                            <span class="condition-status ` + (diabeteInsulin ? 'status-positive' : 'status-negative') + `">
                                ` + (diabeteInsulin ? 'POSITIVE' : 'NEGATIVE') + `
                            </span>
                        </div>
                        <div class="medical-item">
                            <span class="condition-name">Pregnancy</span>
                            <span class="condition-status ` + (grossesse ? 'status-positive' : 'status-negative') + `">
                                ` + (grossesse ? 'POSITIVE' : 'NEGATIVE') + `
                            </span>
                        </div>
                        <div class="medical-item">
                            <span class="condition-name">Breastfeeding</span>
                            <span class="condition-status ` + (allaitement ? 'status-positive' : 'status-negative') + `">
                                ` + (allaitement ? 'POSITIVE' : 'NEGATIVE') + `
                            </span>
                        </div>
                    </div>
                `;

            document.getElementById('assessmentContent').innerHTML = html;
        }

        document.getElementById('medicalModal').style.display = 'flex';
    }

    function closeModal() {
        document.getElementById('medicalModal').style.display = 'none';
    }

    function deleteDonor(donorId, donorName) {
        if (confirm('Are you sure you want to delete donor "' + donorName + '"? This action cannot be undone.')) {
            fetch('${pageContext.request.contextPath}/donor/delete?id=' + donorId, {
                method: 'DELETE'
            }).then(response => {
                if (response.ok) {
                    location.reload();
                } else {
                    alert('Error deleting donor');
                }
            }).catch(error => {
                console.error('Error:', error);
                alert('Error deleting donor');
            });
        }
    }

    window.onclick = function(event) {
        const modal = document.getElementById('medicalModal');
        if (event.target === modal) {
            closeModal();
        }
    }

    document.querySelector('select[name="pageSize"]').addEventListener('change', function() {
        this.form.submit();
    });

    document.querySelector('select[name="status"]').addEventListener('change', function() {
        this.form.submit();
    });
</script>
</body>
</html>