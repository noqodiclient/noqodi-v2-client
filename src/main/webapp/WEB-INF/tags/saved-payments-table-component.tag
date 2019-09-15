
<%@attribute name="tableBodyId" required="true"%>
<%@attribute name="tableHeaderText" required="true"%>


<div class="table-responsive-lg">
    <table class="table table-sm">
        <thead class="thead-light">
        <tr>
            <th scope="col" style="width: 10%">#</th>
            <th scope="col" style="width: 90%">${tableHeaderText}</th>
        </tr>
        </thead>
        <tbody id="${tableBodyId}">
        </tbody>
    </table>
</div>




