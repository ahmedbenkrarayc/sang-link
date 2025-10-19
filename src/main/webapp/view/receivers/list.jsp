<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../fragments/head.jsp"/>
    <style>
        .receiver-card {
            background: white;
            border-radius: 12px;
            box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
            border: 1px solid #e5e7eb;
            transition: all 0.3s ease;
            overflow: hidden;
        }

        .receiver-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.12);
        }

        .card-header {
            padding: 16px;
            color: white;
            position: relative;
        }

        .receiver-card[data-need="CRITICAL"] .card-header {
            background: linear-gradient(135deg, #ef4444, #dc2626);
        }

        .receiver-card[data-need="URGENT"] .card-header {
            background: linear-gradient(135deg, #f59e0b, #d97706);
        }

        .receiver-card[data-need="NORMAL"] .card-header {
            background: linear-gradient(135deg, #10b981, #059669);
        }

        .blood-badge {
            position: absolute;
            top: 16px;
            right: 16px;
            width: 50px;
            height: 50px;
            background: white;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
            font-size: 16px;
            color: #ef4444;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
            border: 2px solid #fef2f2;
        }

        .receiver-name {
            font-size: 18px;
            font-weight: 700;
            margin-bottom: 4px;
        }

        .receiver-cin {
            font-size: 13px;
            opacity: 0.9;
            font-weight: 500;
        }

        .status-badge {
            display: inline-flex;
            align-items: center;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 11px;
            font-weight: 600;
            background: rgba(255, 255, 255, 0.2);
            color: white;
            margin-top: 8px;
        }

        .card-body {
            padding: 16px;
        }

        .info-grid {
            display: grid;
            gap: 12px;
            margin-bottom: 16px;
        }

        .info-item {
            display: flex;
            align-items: center;
            padding: 10px;
            background: #f8fafc;
            border-radius: 8px;
            border: 1px solid #f1f5f9;
        }

        .info-item i {
            width: 16px;
            margin-right: 10px;
            color: #3b82f6;
            font-size: 13px;
        }

        .info-text {
            font-size: 13px;
            color: #374151;
            font-weight: 500;
        }

        .progress-section {
            background: linear-gradient(135deg, #f8fafc, #f1f5f9);
            border: 1px solid #e5e7eb;
            border-radius: 8px;
            padding: 12px;
            margin-bottom: 16px;
        }

        .progress-label {
            font-size: 12px;
            font-weight: 600;
            color: #374151;
            margin-bottom: 6px;
        }

        .progress-bar {
            height: 6px;
            background: #e5e7eb;
            border-radius: 3px;
            overflow: hidden;
            margin-bottom: 6px;
        }

        .progress-fill {
            height: 100%;
            border-radius: 3px;
        }

        .receiver-card[data-need="CRITICAL"] .progress-fill {
            background: #dc2626;
        }

        .receiver-card[data-need="URGENT"] .progress-fill {
            background: #d97706;
        }

        .receiver-card[data-need="NORMAL"] .progress-fill {
            background: #16a34a;
        }

        .progress-count {
            font-size: 11px;
            font-weight: 600;
            color: #6b7280;
            text-align: center;
        }

        .action-buttons {
            display: grid;
            grid-template-columns: 1fr 1fr 1fr;
            gap: 8px;
        }

        .action-btn {
            padding: 10px 8px;
            border: none;
            border-radius: 8px;
            font-size: 11px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.2s ease;
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 4px;
        }

        .action-btn i {
            font-size: 14px;
        }

        .action-btn:hover {
            transform: translateY(-1px);
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

        .btn-link {
            background: #f0fdf4;
            color: #16a34a;
            border: 1px solid #bbf7d0;
        }

        .btn-link:hover {
            background: #16a34a;
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
            border-radius: 16px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
            width: 90%;
            max-width: 600px;
            max-height: 80vh;
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
            border-radius: 16px 16px 0 0;
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

        .donor-list {
            display: grid;
            gap: 12px;
            margin-bottom: 24px;
            max-height: 400px;
            overflow-y: auto;
        }

        .donor-item {
            display: flex;
            align-items: center;
            padding: 16px;
            background: #f8fafc;
            border: 2px solid #e5e7eb;
            border-radius: 12px;
            cursor: pointer;
            transition: all 0.2s ease;
        }

        .donor-item:hover {
            background: #f1f5f9;
            border-color: #3b82f6;
        }

        .donor-item.selected {
            background: #dbeafe;
            border-color: #3b82f6;
        }

        .donor-avatar {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background: linear-gradient(135deg, #10b981, #059669);
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-weight: 600;
            font-size: 14px;
            margin-right: 12px;
        }

        .donor-info {
            flex: 1;
        }

        .donor-name {
            font-weight: 600;
            color: #1f2937;
            margin-bottom: 2px;
        }

        .donor-details {
            font-size: 12px;
            color: #6b7280;
        }

        .donor-blood {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            width: 40px;
            height: 40px;
            background: #fef2f2;
            border: 2px solid #fecaca;
            border-radius: 50%;
            font-weight: bold;
            font-size: 12px;
            color: #dc2626;
        }

        .no-donors {
            text-align: center;
            padding: 40px 20px;
            color: #6b7280;
        }

        .no-donors i {
            font-size: 48px;
            margin-bottom: 16px;
            opacity: 0.5;
        }

        .modal-footer {
            padding: 20px 24px 24px;
            border-top: 1px solid #e5e7eb;
            display: flex;
            justify-content: flex-end;
            gap: 12px;
        }

        .btn-cancel {
            padding: 10px 20px;
            border: 1px solid #d1d5db;
            background: white;
            color: #374151;
            border-radius: 8px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.2s ease;
        }

        .btn-cancel:hover {
            background: #f9fafb;
        }

        .btn-confirm {
            padding: 10px 20px;
            border: none;
            background: #10b981;
            color: white;
            border-radius: 8px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.2s ease;
        }

        .btn-confirm:hover:not(:disabled) {
            background: #059669;
        }

        .btn-confirm:disabled {
            background: #9ca3af;
            cursor: not-allowed;
        }

        .loading-spinner {
            text-align: center;
            padding: 40px 20px;
        }

        .loading-spinner i {
            font-size: 32px;
            color: #3b82f6;
            animation: spin 1s linear infinite;
        }

        @keyframes spin {
            from { transform: rotate(0deg); }
            to { transform: rotate(360deg); }
        }
    </style>
</head>
<body class="bg-gray-50 min-h-screen">
<jsp:include page="../fragments/nav.jsp"/>

<div id="linkDonorModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <button class="close-btn" onclick="closeLinkModal()">
                <i class="fas fa-times"></i>
            </button>
            <div class="modal-title">Link Compatible Donor</div>
            <div class="modal-subtitle" id="modalReceiverName"></div>
        </div>
        <div class="modal-body">
            <div id="donorListContent">
                <!-- Donor list will be loaded here -->
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn-cancel" onclick="closeLinkModal()">Cancel</button>
            <button type="button" class="btn-confirm" id="confirmLinkBtn" disabled onclick="confirmLink()">
                Link Donor
            </button>
        </div>
    </div>
</div>

<div class="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
    <div class="mb-8">
        <div class="flex justify-between items-center">
            <div>
                <h1 class="text-3xl font-bold text-gray-900">Receiver Management</h1>
                <p class="text-gray-600 mt-2">Manage patients in need of blood donations</p>
            </div>
            <a href="${pageContext.request.contextPath}/receiver/create"
               class="inline-flex items-center px-6 py-3 bg-primary text-white font-semibold rounded-xl hover:bg-secondary transition-all duration-200 hover:scale-105 shadow-lg">
                <i class="fas fa-plus mr-2"></i> Add New Receiver
            </a>
        </div>
    </div>

    <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6 mb-8">
        <form method="GET" class="space-y-4 md:space-y-0 md:flex md:items-end md:space-x-6">
            <div class="flex-1 grid grid-cols-1 md:grid-cols-3 gap-6">
                <div>
                    <label class="block text-sm font-semibold text-gray-700 mb-2">Search Receivers</label>
                    <div class="relative">
                        <i class="fas fa-search absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
                        <input type="text" name="search" value="${param.search}"
                               placeholder="Name, CIN, phone..."
                               class="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-primary focus:border-transparent">
                    </div>
                </div>

                <div>
                    <label class="block text-sm font-semibold text-gray-700 mb-2">Filter by Need</label>
                    <select name="need" class="w-full px-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-primary focus:border-transparent">
                        <option value="">All Needs</option>
                        <c:forEach var="need" items="${needs}">
                            <option value="${need}" ${param.need == need ? 'selected' : ''}>${need}</option>
                        </c:forEach>
                    </select>
                </div>

                <div>
                    <label class="block text-sm font-semibold text-gray-700 mb-2">Items per page</label>
                    <select name="pageSize" class="w-full px-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-primary focus:border-transparent">
                        <option value="10" ${param.pageSize == '10' or empty param.pageSize ? 'selected' : ''}>10 receivers</option>
                        <option value="20" ${param.pageSize == '20' ? 'selected' : ''}>20 receivers</option>
                        <option value="30" ${param.pageSize == '30' ? 'selected' : ''}>30 receivers</option>
                    </select>
                </div>
            </div>

            <div class="flex space-x-3">
                <button type="submit"
                        class="flex items-center px-6 py-3 bg-primary text-white rounded-xl hover:bg-secondary transition-colors shadow-lg">
                    <i class="fas fa-filter mr-2"></i> Apply Filters
                </button>
                <a href="${pageContext.request.contextPath}/receivers"
                   class="flex items-center px-6 py-3 border border-gray-300 text-gray-700 rounded-xl hover:bg-gray-50 transition-colors">
                    <i class="fas fa-refresh mr-2"></i> Reset
                </a>
            </div>
        </form>
    </div>

    <c:choose>
        <c:when test="${not empty receivers}">
            <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 2xl:grid-cols-4 gap-6 mb-8">
                <c:forEach var="receiver" items="${receivers}">
                    <c:choose>
                        <c:when test="${receiver.need == 'CRITICAL'}">
                            <c:set var="neededPockets" value="4" />
                        </c:when>
                        <c:when test="${receiver.need == 'URGENT'}">
                            <c:set var="neededPockets" value="3" />
                        </c:when>
                        <c:when test="${receiver.need == 'NORMAL'}">
                            <c:set var="neededPockets" value="1" />
                        </c:when>
                        <c:otherwise>
                            <c:set var="neededPockets" value="1" />
                        </c:otherwise>
                    </c:choose>

                    <!-- Calculate received pockets based on actual donations -->
                    <c:choose>
                        <c:when test="${not empty receiver.donations}">
                            <c:set var="receivedPockets" value="${receiver.donations.size()}" />
                        </c:when>
                        <c:otherwise>
                            <c:set var="receivedPockets" value="0" />
                        </c:otherwise>
                    </c:choose>

                    <!-- Calculate progress percentage -->
                    <c:choose>
                        <c:when test="${neededPockets > 0}">
                            <c:set var="progressPercent" value="${(receivedPockets / neededPockets) * 100}" />
                            <!-- Ensure progress doesn't exceed 100% -->
                            <c:if test="${progressPercent > 100}">
                                <c:set var="progressPercent" value="100" />
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <c:set var="progressPercent" value="0" />
                        </c:otherwise>
                    </c:choose>

                    <div class="receiver-card" data-need="${receiver.need}">
                        <div class="card-header">
                            <div class="blood-badge">
                                <c:choose>
                                    <c:when test="${receiver.bloodGroup == 'A_POSITIVE'}">A+</c:when>
                                    <c:when test="${receiver.bloodGroup == 'A_NEGATIVE'}">A-</c:when>
                                    <c:when test="${receiver.bloodGroup == 'B_POSITIVE'}">B+</c:when>
                                    <c:when test="${receiver.bloodGroup == 'B_NEGATIVE'}">B-</c:when>
                                    <c:when test="${receiver.bloodGroup == 'AB_POSITIVE'}">AB+</c:when>
                                    <c:when test="${receiver.bloodGroup == 'AB_NEGATIVE'}">AB-</c:when>
                                    <c:when test="${receiver.bloodGroup == 'O_POSITIVE'}">O+</c:when>
                                    <c:when test="${receiver.bloodGroup == 'O_NEGATIVE'}">O-</c:when>
                                    <c:otherwise>${receiver.bloodGroup}</c:otherwise>
                                </c:choose>
                            </div>
                            <div class="receiver-name">${receiver.prenom} ${receiver.nom}</div>
                            <div class="receiver-cin">${receiver.cin}</div>
                            <div class="status-badge">
                                    ${receiver.status}
                            </div>
                        </div>

                        <div class="card-body">
                            <div class="info-grid">
                                <div class="info-item">
                                    <i class="fas fa-mobile-alt"></i>
                                    <span class="info-text">${receiver.phone}</span>
                                </div>
                                <div class="info-item">
                                    <i class="fas fa-venus-mars"></i>
                                    <span class="info-text">${receiver.gender}</span>
                                </div>
                                <div class="info-item">
                                    <i class="fas fa-birthday-cake"></i>
                                    <span class="info-text">${receiver.birthday}</span>
                                </div>
                            </div>

                            <div class="progress-section">
                                <div class="progress-label">Blood Progress (${receiver.need})</div>
                                <div class="progress-bar">
                                    <div class="progress-fill" style="width: ${progressPercent}%"></div>
                                </div>
                                <div class="progress-count">${receivedPockets}/${neededPockets} pockets received</div>
                            </div>

                            <div class="action-buttons">
                                <button onclick="editReceiver(${receiver.id})"
                                        class="action-btn btn-edit">
                                    <i class="fas fa-edit"></i>
                                    <span>Edit</span>
                                </button>
                                <button onclick="openLinkModal(${receiver.id}, '${receiver.prenom} ${receiver.nom}')"
                                        class="action-btn btn-link">
                                    <i class="fas fa-link"></i>
                                    <span>Link Donor</span>
                                </button>
                                <button onclick="deleteReceiver(${receiver.id}, '${receiver.prenom} ${receiver.nom}')"
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
                            <span class="font-semibold text-primary">${total}</span> total receivers
                        </div>
                        <div class="flex items-center space-x-2">
                            <c:if test="${currentPage > 1}">
                                <a href="?page=${currentPage - 1}&pageSize=${param.pageSize}&search=${param.search}&need=${param.need}"
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
                                            <a href="?page=${pageNum}&pageSize=${param.pageSize}&search=${param.search}&need=${param.need}"
                                               class="inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-xl text-gray-600 bg-white hover:bg-gray-50 transition duration-200">
                                                    ${pageNum}
                                            </a>
                                        </c:when>
                                    </c:choose>
                                </c:forEach>
                            </div>

                            <c:if test="${currentPage < totalPages}">
                                <a href="?page=${currentPage + 1}&pageSize=${param.pageSize}&search=${param.search}&need=${param.need}"
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
                        <i class="fas fa-user-injured text-gray-400 text-2xl"></i>
                    </div>
                    <h3 class="text-2xl font-bold text-gray-900 mb-3">No receivers found</h3>
                    <p class="text-gray-500 mb-8 text-lg">
                        <c:choose>
                            <c:when test="${not empty param.search or not empty param.need}">
                                No receivers match your search criteria. Try adjusting your filters.
                            </c:when>
                            <c:otherwise>
                                Start by adding the first receiver in need of blood donation.
                            </c:otherwise>
                        </c:choose>
                    </p>
                    <a href="${pageContext.request.contextPath}/receiver/create"
                       class="inline-flex items-center px-8 py-4 bg-primary text-white font-semibold rounded-xl hover:bg-secondary transform hover:scale-105 transition duration-200 shadow-lg">
                        <i class="fas fa-plus mr-3"></i> Add First Receiver
                    </a>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="../fragments/footer.jsp"/>

<script>
    let currentReceiverId = null;
    let selectedDonorId = null;

    function editReceiver(receiverId) {
        window.location.href = '${pageContext.request.contextPath}/receiver/edit?id=' + receiverId;
    }

    function openLinkModal(receiverId, receiverName) {
        currentReceiverId = receiverId;
        selectedDonorId = null;

        document.getElementById('modalReceiverName').textContent = receiverName;
        document.getElementById('confirmLinkBtn').disabled = true;

        document.getElementById('donorListContent').innerHTML =
            '<div class="loading-spinner">' +
            '<i class="fas fa-spinner"></i>' +
            '<div style="margin-top: 16px; font-size: 14px; color: #6b7280;">Loading compatible donors...</div>' +
            '</div>';

        document.getElementById('linkDonorModal').style.display = 'flex';

        fetchCompatibleDonors(receiverId);
    }

    function closeLinkModal() {
        document.getElementById('linkDonorModal').style.display = 'none';
        currentReceiverId = null;
        selectedDonorId = null;
    }

    function fetchCompatibleDonors(receiverId) {
        fetch('${pageContext.request.contextPath}/donor/compatible?receiverId=' + receiverId)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok: ' + response.status);
                }
                return response.json();
            })
            .then(donors => {
                if (!donors || (Array.isArray(donors) && donors.length === 0)) {
                    showNoDonorsMessage();
                } else {
                    displayDonors(donors);
                }
            })
            .catch(error => {
                console.error('Error fetching compatible donors:', error);
                showErrorMessage();
            });
    }

    function showNoDonorsMessage() {
        document.getElementById('donorListContent').innerHTML =
            '<div class="no-donors">' +
            '<i class="fas fa-users-slash"></i>' +
            '<div>No compatible donors found</div>' +
            '<p class="text-sm mt-2 text-gray-500">There are no available donors with compatible blood type.</p>' +
            '</div>';
    }

    function showErrorMessage() {
        document.getElementById('donorListContent').innerHTML =
            '<div class="no-donors">' +
            '<i class="fas fa-exclamation-triangle"></i>' +
            '<div>Error loading donors</div>' +
            '<p class="text-sm mt-2 text-gray-500">Please try again later.</p>' +
            '</div>';
    }

    function displayDonors(donors) {
        if (!donors || !Array.isArray(donors) || donors.length === 0) {
            showNoDonorsMessage();
            return;
        }

        let html = '<div class="donor-list">';

        donors.forEach(donor => {
            if (!donor || !donor.id || !donor.prenom || !donor.nom) {
                console.warn('Invalid donor object:', donor);
                return;
            }

            const bloodGroupDisplay = getBloodGroupDisplay(donor.bloodGroup);
            const initials = (donor.prenom.charAt(0) + donor.nom.charAt(0)).toUpperCase();
            const cin = donor.cin || 'N/A';
            const status = donor.status || 'UNKNOWN';

            html +=
                '<div class="donor-item" onclick="selectDonor(' + donor.id + ', this)">' +
                '<div class="donor-avatar">' + initials + '</div>' +
                '<div class="donor-info">' +
                '<div class="donor-name">' + donor.prenom + ' ' + donor.nom + '</div>' +
                '<div class="donor-details">' +
                cin + ' • ' + status +
                '</div>' +
                '</div>' +
                '<div class="donor-blood">' + bloodGroupDisplay + '</div>' +
                '</div>';
        });

        html += '</div>';
        document.getElementById('donorListContent').innerHTML = html;

        if (document.querySelectorAll('.donor-item').length === 0) {
            showNoDonorsMessage();
        }
    }

    function selectDonor(donorId, element) {
        document.querySelectorAll('.donor-item').forEach(item => {
            item.classList.remove('selected');
        });

        element.classList.add('selected');

        selectedDonorId = donorId;

        document.getElementById('confirmLinkBtn').disabled = false;
    }

    function confirmLink() {
        if (!currentReceiverId || !selectedDonorId) {
            return;
        }

        fetch('${pageContext.request.contextPath}/donation', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'receiverId=' + currentReceiverId + '&donorId=' + selectedDonorId + '&volume=1'
        })
            .then(response => {
                if (response.ok) {
                    closeLinkModal();
                    location.reload();
                } else {
                    alert('Error linking donor to receiver');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error linking donor to receiver');
            });
    }

    function getBloodGroupDisplay(bloodGroup) {
        const bloodGroupMap = {
            'A_POSITIVE': 'A+',
            'A_NEGATIVE': 'A-',
            'B_POSITIVE': 'B+',
            'B_NEGATIVE': 'B-',
            'AB_POSITIVE': 'AB+',
            'AB_NEGATIVE': 'AB-',
            'O_POSITIVE': 'O+',
            'O_NEGATIVE': 'O-'
        };
        return bloodGroupMap[bloodGroup] || bloodGroup;
    }

    function deleteReceiver(receiverId, receiverName) {
        if (confirm('Are you sure you want to delete receiver "' + receiverName + '"? This action cannot be undone.')) {
            fetch('${pageContext.request.contextPath}/receiver/delete?id=' + receiverId, {
                method: 'DELETE'
            }).then(response => {
                if (response.ok) {
                    location.reload();
                } else {
                    alert('Error deleting receiver');
                }
            }).catch(error => {
                console.error('Error:', error);
                alert('Error deleting receiver');
            });
        }
    }

    //Close modal when clicking outside
    window.onclick = function(event) {
        const modal = document.getElementById('linkDonorModal');
        if (event.target === modal) {
            closeLinkModal();
        }
    }

    document.querySelector('select[name="pageSize"]').addEventListener('change', function() {
        this.form.submit();
    });

    document.querySelector('select[name="need"]').addEventListener('change', function() {
        this.form.submit();
    });
</script>
</body>
</html>